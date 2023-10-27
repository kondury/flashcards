package com.github.kondury.flashcards.app.rabbit

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger {}

class RabbitController(private val processors: Set<AbstractRabbitProcessor>) {

    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val scope = CoroutineScope(
        context = dispatcher + CoroutineName("thread-rabbitmq-controller")
    )

    fun start() = scope.launch {
        logger.info { "Cards RabbitMQ controller started" }
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

    fun stop() {
        processors.forEach { it.close() }
    }
}