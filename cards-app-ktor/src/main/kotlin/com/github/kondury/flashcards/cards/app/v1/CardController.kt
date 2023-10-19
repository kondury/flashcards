package com.github.kondury.flashcards.cards.app.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.mappers.v1.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createCard(processor: FcCardProcessor) = processV1(processor)

suspend fun ApplicationCall.readCard(processor: FcCardProcessor) = processV1(processor)

suspend fun ApplicationCall.deleteCard(processor: FcCardProcessor) = processV1(processor)

internal suspend inline fun ApplicationCall.processV1(processor: FcCardProcessor) {
    processor.process(
        { it.fromTransport(receive<IRequest>()) },
        { respond(it.toTransportCard()) }
    )
}



