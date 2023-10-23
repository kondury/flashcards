package com.github.kondury.flashcards.cards.app.kafka

import com.github.kondury.flashcards.cards.common.CardContext

interface TransformationStrategy {
    val inputTopic: String
    val outputTopic: String
    fun serialize(source: CardContext): String
    fun deserialize(value: String, target: CardContext)
}