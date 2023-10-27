package com.github.kondury.flashcards.cards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.AbstractRabbitProcessor
import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.cards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.cards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.helpers.addError
import com.github.kondury.flashcards.cards.common.helpers.asFcError
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toTransportCard
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class CardsV1RabbitProcessor(
    private val processor: FcCardProcessor = FcCardProcessor(),
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
) : AbstractRabbitProcessor(connectionConfig, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(
            { cardContext ->
                apiV1RequestDeserialize<IRequest>(String(message.body)).let {
                    cardContext.fromTransport(it)
                    logger.info { "Request class: ${it::class.simpleName}" }
                }
            },
            { cardContext ->
                val response = cardContext.toTransportCard()
                apiV1ResponseSerialize(response).let {
                    logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }
            }
        )
    }

    override fun Channel.onError(e: Throwable) {
        logger.error { e.stackTraceToString() }
        val context = CardContext().apply {
            state = FcState.FAILING
            addError(error = arrayOf(e.asFcError()))
        }

        val response = context.toTransportCard()
        apiV1ResponseSerialize(response).run {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this.toByteArray())
        }
    }
}