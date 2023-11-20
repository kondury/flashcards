package com.github.kondury.flashcards.cards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.AbstractRabbitProcessor
import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.cards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.cards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.biz.fail
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.helpers.asFcError
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toTransportCard
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery

private val loggerId = {}.javaClass.name.substringBefore("Kt$")

class CardsV1RabbitProcessor(
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
    applicationConfig: CardsApplicationConfig,
) : AbstractRabbitProcessor(connectionConfig, processorConfig),
    CardsApplicationConfig by applicationConfig {

    private val logger = loggerProvider.logger(loggerId)

    override suspend fun Channel.processMessage(message: Delivery) {

        processor.process(
            { cardContext ->
                apiV1RequestDeserialize<IRequest>(String(message.body)).let {
                    cardContext.fromTransport(it)
                    logger.info(
                        msg = "Request class: ${it::class.simpleName}",
                        marker = "DEV"
                    )
                }
            },
            { cardContext ->
                val response = cardContext.toTransportCard()
                apiV1ResponseSerialize(response).let {
                    logger.info(
                        msg = "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}",
                        marker = "DEV"
                    )
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }
            },
            logger,
            "CardsRabbitProcessor"
        )
    }

    override fun Channel.onError(e: Throwable) {
        logger.error(
            msg = e.stackTraceToString(),
            marker = "DEV"
        )
        val context = CardContext().apply { fail(e.asFcError()) }
        val response = context.toTransportCard()
        apiV1ResponseSerialize(response).run {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this.toByteArray())
        }
    }
}