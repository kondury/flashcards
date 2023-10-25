package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.app.rabbit.RabbitController
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor

data class PlacedCardsRabbitConfig(
    val connectionConfig: ConnectionConfig = ConnectionConfig(
        host = "localhost",
        port = 5672,
        user = "guest",
        password = "guest",
    ),
    val fcCardProcessor: FcPlacedCardProcessor = FcPlacedCardProcessor(),
    val v1ProcessorConfig: ProcessorConfig = ProcessorConfig(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "placedcards-exchange",
        queue = "v1-placedcards-queue",
        consumerTag = "v1-placedcards-consumer",
        exchangeType = "direct"
    ),
    val v1RabbitProcessor: V1DirectRabbitProcessor = V1DirectRabbitProcessor(
        processor = fcCardProcessor,
        connectionConfig = connectionConfig,
        processorConfig = v1ProcessorConfig,
    ),
    val controller: RabbitController = RabbitController(
        processors = setOf(v1RabbitProcessor)
    )
)