package com.github.kondury.flashcards.placedcards.app.rabbit.config

import com.github.kondury.flashcards.placedcards.app.rabbit.controller.RabbitController
import com.github.kondury.flashcards.placedcards.app.rabbit.processor.V1DirectRabbitProcessor
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor

data class AppSettings(
    val connectionConfig: ConnectionConfig = ConnectionConfig(),
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