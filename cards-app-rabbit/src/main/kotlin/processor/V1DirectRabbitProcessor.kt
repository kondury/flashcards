package com.github.kondury.flashcards.cards.app.rabbit.processor

import com.github.kondury.flashcards.cards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
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
import kotlinx.datetime.Clock

private val logger = KotlinLogging.logger {}

class V1DirectRabbitProcessor(
    private val processor: FcCardProcessor = FcCardProcessor(),
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
) : BaseRabbitProcessor(connectionConfig, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery) {
        val context = CardContext().apply { timeStart = Clock.System.now() }

        apiV1Mapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this)
            logger.info { "Request class: ${this::class.simpleName}" }
        }

        processor.exec(context)
        val response = context.toTransportCard()

        apiV1Mapper.writeValueAsBytes(response).run {
            logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this)
            logger.info { "published" }
        }
    }

    override fun Channel.onError(e: Throwable) {
        logger.error { e.stackTraceToString() }
        val context = CardContext().apply {
            state = FcState.FAILING
            addError(error = arrayOf(e.asFcError()))
        }
        val response = context.toTransportCard()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}
