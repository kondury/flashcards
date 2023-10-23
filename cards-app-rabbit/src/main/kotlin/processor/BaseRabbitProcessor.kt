package com.github.kondury.flashcards.cards.app.rabbit.processor

import io.github.oshai.kotlinlogging.KotlinLogging
import com.github.kondury.flashcards.cards.app.rabbit.config.ConnectionConfig
import com.github.kondury.flashcards.cards.app.rabbit.config.ProcessorConfig
import com.github.kondury.flashcards.cards.app.rabbit.config.configure
import com.rabbitmq.client.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

/**
 * Абстрактный класс для процессоров-консьюмеров RabbitMQ
 * @property connectionConfig - настройки подключения
 * @property processorConfig - настройки Rabbit exchange
 */
abstract class BaseRabbitProcessor @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val connectionConfig: ConnectionConfig,
    val processorConfig: ProcessorConfig,
    private val dispatcher: CoroutineContext = Dispatchers.IO.limitedParallelism(1) + Job(),
)  {

    private val keepOn = atomic(true)

    suspend fun process() = withContext(dispatcher) {
        ConnectionFactory()
            .configure(connectionConfig)
            .newConnection().use { connection ->
                logger.info { "Creating new connection" }
                connection.createChannel().use { channel ->
                    logger.info { "Creating new channel" }
                    val deliveryCallback = channel.getDeliveryCallback()
                    val cancelCallback = getCancelCallback()
                    channel.describeAndListen(deliveryCallback, cancelCallback)
                }
            }
    }

    protected abstract suspend fun Channel.processMessage(message: Delivery)

    protected abstract fun Channel.onError(e: Throwable)

    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, message ->
        runBlocking {
            kotlin.runCatching {
                processMessage(message)
            }.onFailure {
                onError(it)
            }
        }
    }

    private fun getCancelCallback() = CancelCallback { consumerTag ->
        logger.info { "[$consumerTag] was cancelled" }
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback, cancelCallback: CancelCallback
    ) {
        withContext(Dispatchers.IO) {
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            queueDeclare(processorConfig.queue, false, false, false, null)
            queueBind(processorConfig.queue, processorConfig.exchange, processorConfig.keyIn)
            basicConsume(processorConfig.queue, true, processorConfig.consumerTag, deliverCallback, cancelCallback)

            while (keepOn.value && isOpen) {
                kotlin.runCatching {
                    delay(100)
                }.onFailure(Throwable::printStackTrace)
            }

            logger.info { "Channel for [${processorConfig.consumerTag}] was closed." }
        }
    }

    fun close() {
        logger.info { "Close ${this::class.java.simpleName}" }
        keepOn.value = false
    }
}
