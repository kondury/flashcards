package com.github.kondury.flashcards.cards.app.kafka

data class CardsKafkaSettings(
    override val hosts: List<String> = KAFKA_HOSTS,
    override val groupId: String = KAFKA_GROUP_ID,
    val inTopicV1: String = KAFKA_TOPIC_IN_V1,
    val outTopicV1: String = KAFKA_TOPIC_OUT_V1,
) : KafkaConsumerSettings, KafkaProducerSettings {
    companion object {
        const val KAFKA_HOSTS_ENV = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_ENV = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_ENV = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_ENV = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOSTS_ENV) ?: "localhost:9094").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_ENV) ?: "flashcards-cards" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_ENV) ?: "flashcards-cards-in-v1" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_ENV) ?: "flashcards-cards-out-v1" }
    }
}