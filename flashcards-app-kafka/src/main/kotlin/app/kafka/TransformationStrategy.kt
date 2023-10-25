package com.github.kondury.flashcards.app.kafka

interface TransformationStrategy<T> {
    val inputTopic: String
    val outputTopic: String
    fun serialize(source: T): String
    fun deserialize(value: String, target: T)
}