package com.github.kondury.flashcards.placedcards.app.rabbit.processor

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.*
import com.github.kondury.flashcards.placedcards.app.rabbit.config.ConnectionConfig
import com.github.kondury.flashcards.placedcards.app.rabbit.config.ProcessorConfig
import com.github.kondury.flashcards.placedcards.app.rabbit.config.configure
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.atomic.AtomicBoolean
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
) : AutoCloseable {

    private val run = AtomicBoolean(false)

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

    /**
     * Обработка поступившего сообщения в deliverCallback
     */
    protected abstract suspend fun Channel.processMessage(message: Delivery)

    /**
     * Обработка ошибок
     */
    protected abstract fun Channel.onError(e: Throwable)

    /**
     * Callback, который вызывается при доставке сообщения консьюмеру
     */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, message ->
        runBlocking {
            kotlin.runCatching {
                processMessage(message)
            }.onFailure {
                onError(it)
            }
        }
    }

    /**
     * Callback, вызываемый при отмене консьюмера
     */
    private fun getCancelCallback() = CancelCallback { consumerTag ->
        logger.info { "[$consumerTag] was cancelled" }
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback, cancelCallback: CancelCallback
    ) {
        run.set(true)
        withContext(Dispatchers.IO) {
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            // Объявляем очередь (не сохраняется при перезагрузке сервера; неэксклюзивна - доступна другим соединениям;
            // не удаляется, если не используется)
            queueDeclare(processorConfig.queue, false, false, false, null)
            // связываем обменник с очередью по ключу (сообщения будут поступать в данную очередь с данного обменника при совпадении ключа)
            queueBind(processorConfig.queue, processorConfig.exchange, processorConfig.keyIn)
            // запуск консьюмера с автоотправкой подтверждение при получении сообщения
            basicConsume(processorConfig.queue, true, processorConfig.consumerTag, deliverCallback, cancelCallback)

            while (run.get() && isOpen) {
                kotlin.runCatching {
                    delay(100)
                }.onFailure(Throwable::printStackTrace)
            }

            logger.info { "Channel for [${processorConfig.consumerTag}] was closed." }
        }
    }

    override fun close() {
        logger.info { "Close ${this::class.java.simpleName}" }
        run.set(false)
    }
}
