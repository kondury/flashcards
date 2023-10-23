package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.placedcards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.placedcards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.mappers.v1.fromTransport
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard

data class TransformationStrategyV1(
    override val inputTopic: String,
    override val outputTopic: String
) : TransformationStrategy {

    override fun serialize(source: PlacedCardContext): String {
        val response = source.toTransportPlacedCard()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: PlacedCardContext) {
        val request = apiV1RequestDeserialize<IRequest>(value)
        return target.fromTransport(request)
    }
}