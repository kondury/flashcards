package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.app.rabbit.RabbitController
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.repository.inmemory.InMemoryPlacedCardRepository

data class PlacedCardsRabbitConfig(
    val placedCardsApplicationConfig: PlacedCardsApplicationConfig = object : PlacedCardsApplicationConfig {
        override val loggerProvider: AppLoggerProvider = AppLoggerProvider { getLogbackLogger(it) }
        override val repositoryConfig = PlacedCardRepositoryConfig(
            prodRepository = InMemoryPlacedCardRepository(),
            testRepository = InMemoryPlacedCardRepository(),
        )
        override val corConfig = PlacedCardsCorConfig(repositoryConfig)
        override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
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
        exchange = "placedcards-exchange",
        queue = "v1-placedcards-queue",
        consumerTag = "v1-placedcards-consumer",
        exchangeType = "direct"
    ),
    val v1RabbitProcessor: PlacedCardsV1RabbitProcessor = PlacedCardsV1RabbitProcessor(
        applicationConfig = placedCardsApplicationConfig,
        connectionConfig = connectionConfig,
        processorConfig = v1ProcessorConfig,
    ),
    val controller: RabbitController = RabbitController(
        processors = setOf(v1RabbitProcessor)
    )
)