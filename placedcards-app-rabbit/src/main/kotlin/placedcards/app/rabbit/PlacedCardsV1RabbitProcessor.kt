package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.AbstractRabbitProcessor
import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.placedcards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.placedcards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.app.common.process
import com.github.kondury.flashcards.placedcards.biz.fail
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.asFcError
import com.github.kondury.flashcards.placedcards.mappers.v1.fromTransport
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery


private val loggerId = {}.javaClass.name.substringBefore("Kt$")

class PlacedCardsV1RabbitProcessor(
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
    applicationConfig: PlacedCardsApplicationConfig,
) : AbstractRabbitProcessor(connectionConfig, processorConfig),
    PlacedCardsApplicationConfig by applicationConfig {

    private val logger = loggerProvider.logger(loggerId)

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(
            { placedCardContext ->
                apiV1RequestDeserialize<IRequest>(String(message.body)).let {
                    placedCardContext.fromTransport(it)
                    logger.info(
                        msg = "Request class: ${it::class.simpleName}",
                        marker = "DEV"
                    )
                }
            },
            { placedCardContext ->
                val response = placedCardContext.toTransportPlacedCard()
                apiV1ResponseSerialize(response).let {
                    logger.info(
                        msg = "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}",
                        marker = "DEV"
                    )
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }
            },
            logger,
            "PlacedCardsRabbitProcessor"
        )
    }

    override fun Channel.onError(e: Throwable) {
        logger.error(
            msg = e.stackTraceToString(),
            marker = "DEV"
        )
        val context = PlacedCardContext().apply { fail(e.asFcError()) }
        val response = context.toTransportPlacedCard()
        apiV1ResponseSerialize(response).run {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this.toByteArray())
        }
    }
}