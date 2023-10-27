package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.AbstractRabbitProcessor
import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.placedcards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.placedcards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.app.common.process
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.addError
import com.github.kondury.flashcards.placedcards.common.helpers.asFcError
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.mappers.v1.fromTransport
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class PlacedCardsV1RabbitProcessor(
    private val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(),
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
) : AbstractRabbitProcessor(connectionConfig, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(
            { placedCardContext ->
                apiV1RequestDeserialize<IRequest>(String(message.body)).let {
                    placedCardContext.fromTransport(it)
                    logger.info { "Request class: ${it::class.simpleName}" }
                }
            },
            { placedCardContext ->
                val response = placedCardContext.toTransportPlacedCard()
                apiV1ResponseSerialize(response).let {
                    logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }
            }
        )
    }

    override fun Channel.onError(e: Throwable) {
        logger.error { e.stackTraceToString() }
        val context = PlacedCardContext().apply {
            state = FcState.FAILING
            addError(error = arrayOf(e.asFcError()))
        }
        val response = context.toTransportPlacedCard()
        apiV1ResponseSerialize(response).run {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this.toByteArray())
        }
    }
}