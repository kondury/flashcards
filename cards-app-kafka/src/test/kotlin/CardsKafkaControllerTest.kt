package com.github.kondury.flashcards.cards.app.kafka

import com.github.kondury.flashcards.cards.api.v1.apiV1RequestSerialize
import com.github.kondury.flashcards.cards.api.v1.apiV1ResponseDeserialize
import com.github.kondury.flashcards.cards.api.v1.models.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*
import kotlin.test.Test


class CardsKafkaControllerTest {

    companion object {
        const val PARTITION = 0
        const val INPUT_TOPIC = "input-topic"
        const val OUTPUT_TOPIC = "output-topic"
        const val REQUEST_ID = "12345"

        val serializedRequest = apiV1RequestSerialize(
            CardCreateRequest(
                requestId = REQUEST_ID,
                debug = DebugResource(
                    mode = RunMode.STUB,
                    stub = DebugStub.SUCCESS
                ),
                card = CardCreateResource(front = "Front text", back = "Back text"),
            )
        )
    }

    @Test
    fun `given running kafka when published request then expected response returns`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())

        val config = CardsKafkaConfig(
            settings = CardsKafkaSettings(
                inTopicV1 = INPUT_TOPIC,
                outTopicV1 = OUTPUT_TOPIC,
            ),
            consumer = consumer,
            producer = producer,
        )

        val controller = config.controller

        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(INPUT_TOPIC, 0)))
            consumer.addRecord(
                ConsumerRecord(INPUT_TOPIC, PARTITION, 0L, "test-1", serializedRequest)
            )
            controller.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(INPUT_TOPIC, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        controller.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize(message.value()) as CardCreateResponse
        assertEquals(OUTPUT_TOPIC, message.topic())
        assertEquals(REQUEST_ID, result.requestId)
    }

}


