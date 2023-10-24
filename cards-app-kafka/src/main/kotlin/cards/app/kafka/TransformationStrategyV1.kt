package com.github.kondury.flashcards.cards.app.kafka

import com.github.kondury.flashcards.cards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.cards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toTransportCard

data class TransformationStrategyV1(
    override val inputTopic: String,
    override val outputTopic: String
) : TransformationStrategy {

    override fun serialize(source: CardContext): String {
        val response = source.toTransportCard()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: CardContext) {
        val request = apiV1RequestDeserialize<IRequest>(value)
        return target.fromTransport(request)
    }
}