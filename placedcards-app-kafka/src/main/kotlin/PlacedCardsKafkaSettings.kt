package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.app.kafka.KafkaConsumerSettings
import com.github.kondury.flashcards.app.kafka.KafkaProducerSettings

class PlacedCardsKafkaSettings(
    override val hosts: List<String> = KAFKA_HOSTS,
    override val groupId: String = KAFKA_GROUP_ID,
    val inTopicV1: String = KAFKA_TOPIC_IN_V1,
    val outTopicV1: String = KAFKA_TOPIC_OUT_V1,
) : KafkaProducerSettings, KafkaConsumerSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "localhost:9094").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "flashcards-placedcards" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "flashcards-placedcards-in-v1" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "flashcards-placedcards-out-v1" }
    }
}
