package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

data class PlacedCardsKafkaConfig(
    val settings: PlacedCardsKafkaSettings = PlacedCardsKafkaSettings(),
    val strategies: List<TransformationStrategy> = listOf(TransformationStrategyV1(settings.inTopicV1, settings.outTopicV1)),
    val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(),
    val producer: Producer<String, String> = createKafkaProducer(settings),
    val consumer: Consumer<String, String> = createKafkaConsumer(settings),
    val controller: PlacedCardsKafkaController = PlacedCardsKafkaController(
        strategies = strategies,
        processor = processor,
        consumer = consumer,
        producer = producer,
    )
) {
    companion object {
        private fun createKafkaConsumer(settings: PlacedCardsKafkaSettings): KafkaConsumer<String, String> {
            val props = Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.hosts)
                put(ConsumerConfig.GROUP_ID_CONFIG, settings.groupId)
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            }
            return KafkaConsumer<String, String>(props)
        }

        private fun createKafkaProducer(settings: PlacedCardsKafkaSettings): KafkaProducer<String, String> {
            val props = Properties().apply {
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.hosts)
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            }
            return KafkaProducer<String, String>(props)
        }
    }
}
