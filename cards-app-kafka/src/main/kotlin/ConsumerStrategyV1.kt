package com.github.kondury.flashcards.app.kafka

import com.github.kondury.flashcards.cards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.api.v1.models.IResponse
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toTransportCard

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: CardContext): String {
        val response: IResponse = source.toTransportCard()
        return apiV1Mapper.writeValueAsString(response)
    }

    override fun deserialize(value: String, target: CardContext) {
        val request: IRequest = apiV1Mapper.readValue(value, IRequest::class.java)
        apiV1Mapper.readValue(value, IRequest::class.java)
        target.fromTransport(request)
    }
}