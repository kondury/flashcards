package com.github.kondury.flashcards.mappers.v1

import com.github.kondury.flashcards.api.v1.models.*
import com.github.kondury.flashcards.common.PlacedCardContext
import com.github.kondury.flashcards.common.models.*
import com.github.kondury.flashcards.mappers.v1.exceptions.UnknownRequestClass

fun PlacedCardContext.fromTransport(request: IRequest) = when (request) {
    is PlacedCardCreateRequest -> fromPlacedCardCreateRequest(request)
    is PlacedCardDeleteRequest -> fromPlacedCardDeleteRequest(request)
    is PlacedCardMoveRequest -> fromPlacedCardMoveRequest(request)
    is PlacedCardInitRequest -> fromPlacedCardInitRequest(request)
    is PlacedCardSelectRequest -> fromPlacedCardSelectRequest(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

fun PlacedCardContext.fromPlacedCardCreateRequest(request: PlacedCardCreateRequest) {
    command = PlacedCardCommand.CREATE_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    placedCardRequest = request.placedCard?.toInternal() ?: PlacedCard()
}

fun PlacedCardContext.fromPlacedCardDeleteRequest(request: PlacedCardDeleteRequest) {
    command = PlacedCardCommand.DELETE_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    placedCardRequest = request.placedCard?.id.toPlacedCardWithId()
}

fun PlacedCardContext.fromPlacedCardMoveRequest(request: PlacedCardMoveRequest) {
    command = PlacedCardCommand.MOVE_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    placedCardRequest = request.move?.id.toPlacedCardWithId()
    boxAfter = request.move?.box.fromTransport()
}

fun PlacedCardContext.fromPlacedCardInitRequest(request: PlacedCardInitRequest) {
    command = PlacedCardCommand.INIT_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    ownerId = request.init?.ownerId.toUserId()
    workBox = request.init?.box.fromTransport()
}

fun PlacedCardContext.fromPlacedCardSelectRequest(request: PlacedCardSelectRequest) {
    command = PlacedCardCommand.SELECT_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    ownerId = request.select?.ownerId.toUserId()
    workBox = request.select?.box.fromTransport()
    searchStrategy = request.select?.searchStrategy.fromTransport()
}

private fun PlacedCardCreateResource.toInternal(): PlacedCard = PlacedCard(
    ownerId = this.ownerId.toUserId(),
    box = this.box.fromTransport(),
    cardId = this.cardId.toCardId(),
)

private fun String?.toPlacedCardId() = this?.let { PlacedCardId(it) } ?: PlacedCardId.NONE

private fun String?.toPlacedCardWithId() = PlacedCard(id = this.toPlacedCardId())

private fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE

private fun Box?.fromTransport(): FcBox = when (this) {
    Box.NEW -> FcBox.NEW
    Box.REPEAT -> FcBox.REPEAT
    Box.FINISHED -> FcBox.FINISHED
    null -> FcBox.NONE
}

private fun SearchStrategy?.fromTransport(): FcSearchStrategy = when (this) {
    SearchStrategy.EARLIEST_CREATED -> FcSearchStrategy.EARLIEST_CREATED
    SearchStrategy.EARLIEST_REVIEWED -> FcSearchStrategy.EARLIEST_REVIEWED
    null -> FcSearchStrategy.NONE
}
