package com.github.kondury.flashcards.placedcards.app.rabbit.controller

import kotlinx.coroutines.*
import com.github.kondury.flashcards.placedcards.app.rabbit.processor.BaseRabbitProcessor
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.Executors


private val logger = KotlinLogging.logger {}

class RabbitController(private val processors: Set<BaseRabbitProcessor>) {

    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val scope = CoroutineScope(
        context = dispatcher + CoroutineName("thread-rabbitmq-controller")
    )

    fun start() = scope.launch {
        logger.info { "PlacedCards RabbitMQ controller started" }
        processors.forEach {
            launch(
                dispatcher + CoroutineName("thread-${it.processorConfig.consumerTag}")
            ) {
                try {
                    it.process()
                } catch (e: RuntimeException) {
                    logger.error { e.stackTraceToString() }
                }
            }
        }
    }

    fun close() {
        processors.forEach { it.close() }
    }
}
