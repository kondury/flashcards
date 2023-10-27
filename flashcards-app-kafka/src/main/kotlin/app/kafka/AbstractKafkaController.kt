package com.github.kondury.flashcards.app.kafka

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

abstract class AbstractKafkaController<T>(
    strategies: List<TransformationStrategy<T>>,
    private val consumer: Consumer<String, String>,
    private val producer: Producer<String, String>
) {
    private val strategiesByInput: Map<String, TransformationStrategy<T>> = strategies.associateBy { it.inputTopic }
    private val keepOn = atomic(true)

    fun run() = runBlocking {
        try {
            consumer.subscribe(strategiesByInput.keys)
            while (keepOn.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }

                if (!records.isEmpty)
                    logger.info { "Received ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        val strategy = strategiesByInput[record.topic()]
                            ?: throw RuntimeException("Received message from unknown topic ${record.topic()}")

                        logger.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                        process(strategy, record)

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

    protected abstract suspend fun process(
        strategy: TransformationStrategy<T>,
        record: ConsumerRecord<String, String>
    )

    protected fun send(topic: String, value: String) {
        val key = UUID.randomUUID().toString()
        val record = ProducerRecord(topic, key, value)
        logger.info { "sending $key to $topic:\n$value" }
        producer.send(record)
    }

    fun stop() {
        keepOn.value = false
    }
}