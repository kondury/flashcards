package com.github.kondury.flashcards.app.kafka

import com.github.kondury.flashcards.cards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toTransportCard

class ConsumerStrategyV1 : ConsumerStrategy {

    override fun topics(config: AppKafkaConfig) =
        InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)

    override fun serialize(source: CardContext): String =
        source.toTransportCard()
            .let(apiV1Mapper::writeValueAsString)

    override fun deserialize(value: String, target: CardContext) =
        apiV1Mapper.readValue(value, IRequest::class.java)
            .let(target::fromTransport)
}