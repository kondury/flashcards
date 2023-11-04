package com.github.kondury.flashcards.placedcards.app.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.app.common.process
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.mappers.v1.fromTransport
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private val loggerId = {}.javaClass.name.substringBefore("Kt$")

suspend fun ApplicationCall.createPlacedCard(config: PlacedCardsApplicationConfig) =
    processV1(config, "httpCreatePlacedCard")

suspend fun ApplicationCall.movePlacedCard(config: PlacedCardsApplicationConfig) =
    processV1(config, "httpMovePlacedCard")

suspend fun ApplicationCall.deletePlacedCard(config: PlacedCardsApplicationConfig) =
    processV1(config, "httpDeletePlacedCard")

suspend fun ApplicationCall.selectPlacedCard(config: PlacedCardsApplicationConfig) =
    processV1(config, "httpSelectPlacedCard")

suspend fun ApplicationCall.initPlacedCard(config: PlacedCardsApplicationConfig) =
    processV1(config, "httpInitPlacedCard")

internal suspend inline fun ApplicationCall.processV1(config: PlacedCardsApplicationConfig, logId: String) {
    val processor = config.processor
    val logger = config.loggerProvider.logger(loggerId)
    processor.process(
        { placedCardContext ->  placedCardContext.fromTransport(receive<IRequest>()) },
        { placedCardContext ->  respond(placedCardContext.toTransportPlacedCard()) },
        logger,
        logId
    )
}


