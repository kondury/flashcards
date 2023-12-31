package com.github.kondury.flashcards.cards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.app.rabbit.RabbitController
import com.github.kondury.flashcards.cards.app.common.AuthConfig
import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.repository.inmemory.InMemoryCardRepository
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger

data class CardsRabbitConfig(
    val cardsApplicationConfig: CardsApplicationConfig = object : CardsApplicationConfig {
        override val loggerProvider = AppLoggerProvider { getLogbackLogger(it) }
        override val repositoryConfig = CardRepositoryConfig (
            prodRepository = InMemoryCardRepository(),
            testRepository = InMemoryCardRepository()
        )
        override val corConfig: CardsCorConfig = CardsCorConfig(repositoryConfig)
        override val processor: FcCardProcessor = FcCardProcessor(corConfig)
        override val auth: AuthConfig = AuthConfig.NONE
    },
    val connectionConfig: ConnectionConfig = ConnectionConfig(
        host = "localhost",
        port = 5672,
        user = "guest",
        password = "guest",
    ),
    val v1ProcessorConfig: ProcessorConfig = ProcessorConfig(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "cards-exchange",
        queue = "v1-cards-queue",
        consumerTag = "v1-cards-consumer",
        exchangeType = "direct",
    ),
    val v1RabbitProcessor: CardsV1RabbitProcessor = CardsV1RabbitProcessor(
        applicationConfig = cardsApplicationConfig,
        connectionConfig = connectionConfig,
        processorConfig = v1ProcessorConfig,
    ),
    val controller: RabbitController = RabbitController(
        processors = setOf(v1RabbitProcessor)
    )
)