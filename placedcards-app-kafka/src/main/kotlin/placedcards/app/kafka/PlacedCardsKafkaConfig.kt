package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.app.kafka.TransformationStrategy
import com.github.kondury.flashcards.app.kafka.createKafkaConsumer
import com.github.kondury.flashcards.app.kafka.createKafkaProducer
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.repository.inmemory.InMemoryPlacedCardRepository
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer


data class PlacedCardsKafkaConfig(
    val applicationConfig: PlacedCardsApplicationConfig = object : PlacedCardsApplicationConfig {
        override val loggerProvider: AppLoggerProvider = AppLoggerProvider { getLogbackLogger(it) }
        override val repositoryConfig = PlacedCardRepositoryConfig(
            prodRepository = InMemoryPlacedCardRepository(),
            testRepository = InMemoryPlacedCardRepository(),
        )
        override val corConfig = PlacedCardsCorConfig(repositoryConfig)
        override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
        override val auth: AuthConfig = AuthConfig.NONE
    },
    val settings: PlacedCardsKafkaSettings = PlacedCardsKafkaSettings(),
    val strategies: List<TransformationStrategy<PlacedCardContext>> = listOf(
        PlacedCardsTransformationStrategyV1(
            settings.inTopicV1,
            settings.outTopicV1
        )
    ),
    val producer: Producer<String, String> = settings.createKafkaProducer(),
    val consumer: Consumer<String, String> = settings.createKafkaConsumer(),
    val controller: PlacedCardsKafkaController = PlacedCardsKafkaController(applicationConfig, strategies, consumer, producer)
)
