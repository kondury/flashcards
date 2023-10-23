package com.github.kondury.flashcards.cards.app.rabbit.processor

import com.github.kondury.flashcards.cards.api.v1.apiV1RequestDeserialize
import com.github.kondury.flashcards.cards.api.v1.apiV1ResponseSerialize
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.app.rabbit.config.ConnectionConfig
import com.github.kondury.flashcards.cards.app.rabbit.config.ProcessorConfig
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

class V1DirectRabbitProcessor(
    private val processor: FcCardProcessor = FcCardProcessor(),
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
) : BaseRabbitProcessor(connectionConfig, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(
            { cardContext ->
                apiV1RequestDeserialize<IRequest>(String(message.body)).run {
                    cardContext.fromTransport(this)
                    logger.info { "Request class: ${this::class.simpleName}" }
                }
            },
            { cardContext ->
                val response = cardContext.toTransportCard()
                apiV1ResponseSerialize(response).run {
                    logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this.toByteArray())
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
