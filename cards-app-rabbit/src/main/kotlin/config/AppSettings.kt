package com.github.kondury.flashcards.cards.app.rabbit.config

import com.github.kondury.flashcards.cards.app.rabbit.controller.RabbitController
import com.github.kondury.flashcards.cards.app.rabbit.processor.V1DirectRabbitProcessor
import com.github.kondury.flashcards.cards.biz.FcCardProcessor

data class AppSettings(
    val connectionConfig: ConnectionConfig = ConnectionConfig(),
    val fcCardProcessor: FcCardProcessor = FcCardProcessor(),
    val v1ProcessorConfig: ProcessorConfig = ProcessorConfig(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "cards-exchange",
        queue = "v1-cards-queue",
        consumerTag = "v1-cards-consumer",
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