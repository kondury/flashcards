package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.mappers.v1.exceptions.UnknownRequestClass


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

    requestPlacedCard = request.placedCard?.toInternal() ?: PlacedCard.EMPTY
}

fun PlacedCardContext.fromPlacedCardDeleteRequest(request: PlacedCardDeleteRequest) {
    command = PlacedCardCommand.DELETE_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    requestPlacedCardId = request.placedCard?.id.toPlacedCardId()
}

fun PlacedCardContext.fromPlacedCardMoveRequest(request: PlacedCardMoveRequest) {
    command = PlacedCardCommand.MOVE_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    requestPlacedCardId = request.move?.id.toPlacedCardId()
    requestBoxAfter = request.move?.box.fromTransport()
}

fun PlacedCardContext.fromPlacedCardInitRequest(request: PlacedCardInitRequest) {
    command = PlacedCardCommand.INIT_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    requestOwnerId = request.init?.ownerId.toUserId()
    requestWorkBox = request.init?.box.fromTransport()
}

fun PlacedCardContext.fromPlacedCardSelectRequest(request: PlacedCardSelectRequest) {
    command = PlacedCardCommand.SELECT_PLACED_CARD
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    requestId = request.requestId()

    requestOwnerId = request.select?.ownerId.toUserId()
    requestWorkBox = request.select?.box.fromTransport()
    requestSearchStrategy = request.select?.searchStrategy.fromTransport()
}

private fun PlacedCardCreateResource.toInternal(): PlacedCard = PlacedCard(
    ownerId = this.ownerId.toUserId(),
    box = this.box.fromTransport(),
    cardId = this.cardId.toCardId(),
)

//private fun String?.toPlacedCardWithId() = PlacedCard(id = this.toPlacedCardId())

private fun String?.toPlacedCardId() = this?.let { PlacedCardId(it) } ?: PlacedCardId.NONE
private fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE
private fun String?.toCardId(): CardId = this?.let { CardId(it) } ?: CardId.NONE

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


