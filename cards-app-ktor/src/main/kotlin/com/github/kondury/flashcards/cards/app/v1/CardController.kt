package com.github.kondury.flashcards.cards.app.v1

import com.github.kondury.flashcards.cards.api.v1.models.CardCreateRequest
import com.github.kondury.flashcards.cards.api.v1.models.CardDeleteRequest
import com.github.kondury.flashcards.cards.api.v1.models.CardReadRequest
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toCardCreateResponse
import com.github.kondury.flashcards.cards.mappers.v1.toCardDeleteResponse
import com.github.kondury.flashcards.cards.mappers.v1.toCardReadResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*


suspend fun ApplicationCall.createCard(processor: FcCardProcessor) {
    val request = receive<CardCreateRequest>()
    val context = CardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toCardCreateResponse())
}

suspend fun ApplicationCall.readCard(processor: FcCardProcessor) {
    val request = receive<CardReadRequest>()
    val context = CardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toCardReadResponse())
}

suspend fun ApplicationCall.deleteCard(processor: FcCardProcessor) {
    val request = receive<CardDeleteRequest>()
    val context = CardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toCardDeleteResponse())
}

