package com.github.kondury.flashcards.cards.app.kafka

import com.github.kondury.flashcards.app.kafka.TransformationStrategy
import com.github.kondury.flashcards.app.kafka.createKafkaConsumer
import com.github.kondury.flashcards.app.kafka.createKafkaProducer
import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.repository.inmemory.InMemoryCardRepository
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer

data class CardsKafkaConfig(
    val applicationConfig: CardsApplicationConfig = object : CardsApplicationConfig {
        override val loggerProvider = AppLoggerProvider { getLogbackLogger(it) }
        override val repositoryConfig = CardRepositoryConfig(
            prodRepository = InMemoryCardRepository(),
            testRepository = InMemoryCardRepository()
        )
        override val corConfig = CardsCorConfig(repositoryConfig)
        override val processor = FcCardProcessor(corConfig)
    },
    val settings: CardsKafkaSettings = CardsKafkaSettings(),
    val strategies: List<TransformationStrategy<CardContext>> = listOf(
        CardsTransformationStrategyV1(
            settings.inTopicV1,
            settings.outTopicV1
        )
    ),
    val producer: Producer<String, String> = settings.createKafkaProducer(),
    val consumer: Consumer<String, String> = settings.createKafkaConsumer(),
    val controller: CardsKafkaController = CardsKafkaController(applicationConfig, strategies, consumer, producer)
)
