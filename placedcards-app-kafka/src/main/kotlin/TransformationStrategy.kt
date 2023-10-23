package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext

interface TransformationStrategy {
    val inputTopic: String
    val outputTopic: String
    fun serialize(source: PlacedCardContext): String
    fun deserialize(value: String, target: PlacedCardContext)
}