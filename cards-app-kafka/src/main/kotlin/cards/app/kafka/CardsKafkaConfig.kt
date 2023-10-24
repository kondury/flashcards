package com.github.kondury.flashcards.cards.app.kafka

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer

data class CardsKafkaConfig(
    val settings: CardsKafkaSettings = CardsKafkaSettings(),
    val strategies: List<TransformationStrategy> = listOf(
        TransformationStrategyV1(
            settings.inTopicV1,
            settings.outTopicV1
        )
    ),
    val processor: FcCardProcessor = FcCardProcessor(),
    val producer: Producer<String, String> = settings.createKafkaProducer(),
    val consumer: Consumer<String, String> = settings.createKafkaConsumer(),
    val controller: CardsKafkaController = CardsKafkaController(strategies, processor, consumer, producer)
)
