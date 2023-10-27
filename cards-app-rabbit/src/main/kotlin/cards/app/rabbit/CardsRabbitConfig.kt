package com.github.kondury.flashcards.cards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.app.rabbit.RabbitController
import com.github.kondury.flashcards.cards.biz.FcCardProcessor

data class CardsRabbitConfig(
    val connectionConfig: ConnectionConfig = ConnectionConfig(
        host = "localhost",
        port = 5672,
        user = "guest",
        password = "guest",
    ),
    val fcCardProcessor: FcCardProcessor = FcCardProcessor(),
    val v1ProcessorConfig: ProcessorConfig = ProcessorConfig(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "cards-exchange",
        queue = "v1-cards-queue",
        consumerTag = "v1-cards-consumer",
        exchangeType = "direct",
    ),
    val v1RabbitProcessor: CardsV1RabbitProcessor = CardsV1RabbitProcessor(
        processor = fcCardProcessor,
        connectionConfig = connectionConfig,
        processorConfig = v1ProcessorConfig,
    ),
    val controller: RabbitController = RabbitController(
        processors = setOf(v1RabbitProcessor)
    )
)