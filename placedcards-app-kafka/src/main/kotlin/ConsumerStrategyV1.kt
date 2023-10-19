package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.placedcards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.placedcards.api.v1.apiV1ResponseDeserialize
import com.github.kondury.flashcards.placedcards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.api.v1.models.IResponse
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.mappers.v1.fromTransport
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: PlacedCardContext): String {
        val response: IResponse = source.toTransportPlacedCard()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: PlacedCardContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}