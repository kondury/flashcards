package com.github.kondury.flashcards.placedcards.app.rabbit.processor

import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.app.rabbit.config.ConnectionConfig
import com.github.kondury.flashcards.placedcards.app.rabbit.config.ProcessorConfig
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
import kotlinx.datetime.Clock

private val logger = KotlinLogging.logger {}

class V1DirectRabbitProcessor(
    private val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(),
    connectionConfig: ConnectionConfig,
    processorConfig: ProcessorConfig,
) : BaseRabbitProcessor(connectionConfig, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery) {
        val context = PlacedCardContext().apply { timeStart = Clock.System.now() }

        apiV1Mapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this)
            logger.info { "Request class: ${this::class.simpleName}" }
        }

        processor.exec(context)
        val response = context.toTransportPlacedCard()

        apiV1Mapper.writeValueAsBytes(response).run {
            logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, this)
            logger.info { "published" }
        }
    }

    override fun Channel.onError(e: Throwable) {
        logger.error { e.stackTraceToString() }
        val context = PlacedCardContext().apply {
            state = FcState.FAILING
            addError(error = arrayOf(e.asFcError()))
        }
        val response = context.toTransportPlacedCard()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}
