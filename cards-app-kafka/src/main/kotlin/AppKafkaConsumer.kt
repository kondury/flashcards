package com.github.kondury.flashcards.app.kafka

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.util.*

private val logger = KotlinLogging.logger {}

data class InputOutputTopics(val input: String, val output: String)

interface ConsumerStrategy {
    fun topics(config: AppKafkaConfig): InputOutputTopics
    fun serialize(source: CardContext): String
    fun deserialize(value: String, target: CardContext)
}

class AppKafkaConsumer(
    private val config: AppKafkaConfig,
    consumerStrategies: List<ConsumerStrategy>,
    private val processor: FcCardProcessor = FcCardProcessor(),
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) {

    private val keepOn = atomic(true)

    private val topicsAndStrategyByInputTopic: Map<String, TopicsAndStrategy> = consumerStrategies.associate {
        val topics = it.topics(config)
        topics.input to TopicsAndStrategy(topics.input, topics.output, it)
    }

    fun run() = runBlocking {
        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (keepOn.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty)
                    logger.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        val ctx = CardContext(
                            timeStart = Clock.System.now(),
                        )
                        logger.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        strategy.deserialize(record.value(), ctx)
                        processor.exec(ctx)

                        sendResponse(ctx, strategy, outputTopic)
                    } catch (ex: Exception) {
                        logger.error(ex) { "error" }
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: CardContext, strategy: ConsumerStrategy, outputTopic: String) {
        val json = strategy.serialize(context)
        val resRecord = ProducerRecord(
            outputTopic,
            UUID.randomUUID().toString(),
            json
        )
        logger.info { "sending ${resRecord.key()} to $outputTopic:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        keepOn.value = false
    }

    private data class TopicsAndStrategy(
        val inputTopic: String,
        val outputTopic: String,
        val strategy: ConsumerStrategy
    )
}
