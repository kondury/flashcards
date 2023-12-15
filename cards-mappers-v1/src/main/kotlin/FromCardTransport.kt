package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcRequestId
import com.github.kondury.flashcards.cards.mappers.v1.exceptions.UnknownRequestClass

fun CardContext.fromTransport(request: IRequest) = when (request) {
    is CardCreateRequest -> fromCardCreateRequest(request)
    is CardDeleteRequest -> fromCardDeleteRequest(request)
    is CardReadRequest -> fromCardReadRequest(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

fun CardContext.fromCardCreateRequest(request: CardCreateRequest) {
    fromTransportCommon(CardCommand.CREATE_CARD, request.debug, request)
    requestCard = request.card.mapOrDefault(Card.EMPTY) {
        Card(
            front = it.front.orEmpty(),
            back = it.back.orEmpty()
        )
    }
}

fun CardContext.fromCardDeleteRequest(request: CardDeleteRequest) {
    fromTransportCommon(CardCommand.DELETE_CARD, request.debug, request)
    requestCard = request.card.mapOrDefault(Card.EMPTY) {
        Card(
            id = it.id.mapOrDefault(CardId.NONE, ::CardId),
        )
    }
}

fun CardContext.fromCardReadRequest(request: CardReadRequest) {
    fromTransportCommon(CardCommand.READ_CARD, request.debug, request)
    requestCard = request.card.mapOrDefault(Card.EMPTY) {
        Card(id = it.id.mapOrDefault(CardId.NONE, ::CardId))
    }
}

private fun CardContext.fromTransportCommon(cmd: CardCommand, debug: DebugResource?, request: IRequest) {
    command = cmd
    workMode = debug.toWorkMode()
    stubCase = debug.toStubCaseOrNone()
    requestId = request.requestId.mapOrDefault(FcRequestId.NONE, ::FcRequestId)
}
