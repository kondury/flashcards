package com.github.kondury.flashcards.app.kafka

interface KafkaSettings {
    val hosts: List<String>
}

interface KafkaConsumerSettings : KafkaSettings {
    val groupId: String
}

interface KafkaProducerSettings : KafkaSettings