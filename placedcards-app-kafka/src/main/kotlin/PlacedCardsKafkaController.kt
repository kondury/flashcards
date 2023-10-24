package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.placedcards.app.common.process
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.util.*

private val logger = KotlinLogging.logger {}

class PlacedCardsKafkaController(
    strategies: List<TransformationStrategy>,
    private val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(),
    private val consumer: Consumer<String, String>,
    private val producer: Producer<String, String>
) {
    private val strategiesByInput: Map<String, TransformationStrategy> = strategies.associateBy { it.inputTopic }
    private val keepOn = atomic(true)

    fun run() = runBlocking {
        try {
            consumer.subscribe(strategiesByInput.keys)
            while (keepOn.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty)
                    logger.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        logger.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }

                        val strategy = strategiesByInput[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        processor.process(
                            { placedCardContext -> strategy.deserialize(record.value(), placedCardContext) },
                            { placedCardContext ->
                                val json = strategy.serialize(placedCardContext)
                                send(strategy.outputTopic, json)
                            }
                        )
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

    private fun send(topic: String, value: String) {
        val key = UUID.randomUUID().toString()
        val record = ProducerRecord(topic, key, value)
        logger.info { "sending $key to $topic:\n$value" }
        producer.send(record)
    }

    fun stop() {
        keepOn.value = false
    }
}
