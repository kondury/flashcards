package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.mappers.v1.exceptions.UnknownRequestClass

fun CardContext.fromTransport(request: IRequest) = when (request) {
    is CardCreateRequest -> fromCardCreateRequest(request)
    is CardDeleteRequest -> fromCardDeleteRequest(request)
    is CardReadRequest -> fromCardReadRequest(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

fun CardContext.fromCardCreateRequest(request: CardCreateRequest) {
    command = CardCommand.CREATE_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    cardRequest = request.card?.toInternal() ?: Card()
}

fun CardContext.fromCardDeleteRequest(request: CardDeleteRequest) {
    command = CardCommand.DELETE_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    cardRequest = request.card?.id.toCardWithId()
}

fun CardContext.fromCardReadRequest(request: CardReadRequest) {
    command = CardCommand.READ_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    cardRequest = request.card?.id.toCardWithId()
}

private fun CardCreateResource.toInternal(): Card = Card(
    front = this.front ?: "",
    back = this.back ?: "",
)

private fun String?.toCardWithId() = Card(id = this.toCardId())

