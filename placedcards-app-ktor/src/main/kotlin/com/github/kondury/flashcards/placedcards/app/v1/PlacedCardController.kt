package com.github.kondury.flashcards.placedcards.app.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.mappers.v1.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*


suspend fun ApplicationCall.createPlacedCard(processor: FcPlacedCardProcessor) {
    val request = receive<PlacedCardCreateRequest>()
    val context = PlacedCardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toPlacedCardCreateResponse())
}

suspend fun ApplicationCall.movePlacedCard(processor: FcPlacedCardProcessor) {
    val request = receive<PlacedCardMoveRequest>()
    val context = PlacedCardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toPlacedCardMoveResponse())
}

suspend fun ApplicationCall.deletePlacedCard(processor: FcPlacedCardProcessor) {
    val request = receive<PlacedCardDeleteRequest>()
    val context = PlacedCardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toPlacedCardDeleteResponse())
}

suspend fun ApplicationCall.selectPlacedCard(processor: FcPlacedCardProcessor) {
    val request = receive<PlacedCardSelectRequest>()
    val context = PlacedCardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toPlacedCardSelectResponse())
}

suspend fun ApplicationCall.initPlacedCard(processor: FcPlacedCardProcessor) {
    val request = receive<PlacedCardInitRequest>()
    val context = PlacedCardContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toPlacedCardInitResponse())
}


