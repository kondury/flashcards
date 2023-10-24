package com.github.kondury.flashcards.cards.app.kafka

interface KafkaSettings {
    val hosts: List<String>
}

interface KafkaConsumerSettings : KafkaSettings {
    val groupId: String
}

interface KafkaProducerSettings : KafkaSettings
