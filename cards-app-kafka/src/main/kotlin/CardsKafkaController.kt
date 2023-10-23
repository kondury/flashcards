package com.github.kondury.flashcards.app.kafka

import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
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

class CardsKafkaController(
    strategies: List<TransformationStrategy>,
    private val processor: FcCardProcessor,
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
//                println("I am HERE")
                if (!records.isEmpty)
                    logger.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        logger.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }

                        val strategy = strategiesByInput[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        processor.process(
                            { cardContext -> strategy.deserialize(record.value(), cardContext) },
                            { cardContext ->
                                val json = strategy.serialize(cardContext)
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

    private fun send(outputTopic: String, json: String) {
        val resRecord = ProducerRecord(
            outputTopic,
            UUID.randomUUID().toString(),
            json
        )
        logger.info { "sending ${resRecord.key()} to ${outputTopic}:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        keepOn.value = false
    }
}
