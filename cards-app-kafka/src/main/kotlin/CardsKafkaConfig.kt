package com.github.kondury.flashcards.app.kafka

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

data class CardsKafkaConfig(
    val settings: CardsKafkaSettings = CardsKafkaSettings().also { println(this) },
    val strategies: List<TransformationStrategy> = listOf(TransformationStrategyV1(settings.inTopicV1, settings.outTopicV1)),
    val processor: FcCardProcessor = FcCardProcessor(),
    val producer: Producer<String, String> = createKafkaProducer(settings).also { println("PRODUCER CREATED") },
    val consumer: Consumer<String, String> = createKafkaConsumer(settings).also { println("CONSUMER CREATED") },
    val controller: CardsKafkaController = CardsKafkaController(
        strategies = strategies.also { println(this) },
        processor = processor,
        consumer = consumer,
        producer = producer,
    )
) {
    companion object {
        private fun createKafkaConsumer(settings: CardsKafkaSettings): KafkaConsumer<String, String> {
            println("CREATE KAFKA CONSUMER")
            println(settings)
            val props = Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.hosts)
                put(ConsumerConfig.GROUP_ID_CONFIG, settings.groupId)
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            }
            return KafkaConsumer<String, String>(props)
        }

        private fun createKafkaProducer(settings: CardsKafkaSettings): KafkaProducer<String, String> {
            println("CREATE KAFKA PRODUCER")
            println(settings)
            val props = Properties().apply {
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.hosts)
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            }
            return KafkaProducer<String, String>(props)
        }
    }
}