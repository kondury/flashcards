package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.placedcards.api.v1.models.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*


class KafkaControllerTest {

    companion object {
        const val PARTITION = 0

        const val REQUEST_ID = "12345"
        private val debugResource = DebugResource(
            mode = RunMode.STUB,
            stub = DebugStub.SUCCESS,
        )
    }

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic, PARTITION, 0L, "test-1", apiV1Mapper.writeValueAsString(
                        PlacedCardCreateRequest(
                            requestId = REQUEST_ID,
                            debug = debugResource,
                            placedCard = PlacedCardCreateResource(),
                        )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1Mapper.readValue(message.value(), PlacedCardCreateResponse::class.java) as PlacedCardCreateResponse
        assertEquals(outputTopic, message.topic())
        assertEquals(REQUEST_ID, result.requestId)
    }

}


