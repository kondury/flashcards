package com.github.kondury.flashcards.placedcards.app.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.app.common.process
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.mappers.v1.fromTransport
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*


suspend fun ApplicationCall.createPlacedCard(processor: FcPlacedCardProcessor) = processV1(processor)

suspend fun ApplicationCall.movePlacedCard(processor: FcPlacedCardProcessor) = processV1(processor)

suspend fun ApplicationCall.deletePlacedCard(processor: FcPlacedCardProcessor) = processV1(processor)

suspend fun ApplicationCall.selectPlacedCard(processor: FcPlacedCardProcessor) = processV1(processor)

suspend fun ApplicationCall.initPlacedCard(processor: FcPlacedCardProcessor) = processV1(processor)

internal suspend inline fun ApplicationCall.processV1(processor: FcPlacedCardProcessor) {
    processor.process(
        { it.fromTransport(receive<IRequest>()) },
        { respond(it.toTransportPlacedCard()) }
    )
}


