package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.app.kafka.TransformationStrategy
import com.github.kondury.flashcards.app.kafka.createKafkaConsumer
import com.github.kondury.flashcards.app.kafka.createKafkaProducer
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer

data class PlacedCardsKafkaConfig(
    val settings: PlacedCardsKafkaSettings = PlacedCardsKafkaSettings(),
    val strategies: List<TransformationStrategy<PlacedCardContext>> = listOf(
        PlacedCardsTransformationStrategyV1(
            settings.inTopicV1,
            settings.outTopicV1
        )
    ),
    val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(),
    val producer: Producer<String, String> = settings.createKafkaProducer(),
    val consumer: Consumer<String, String> = settings.createKafkaConsumer(),
    val controller: PlacedCardsKafkaController = PlacedCardsKafkaController(strategies, processor, consumer, producer)
)
