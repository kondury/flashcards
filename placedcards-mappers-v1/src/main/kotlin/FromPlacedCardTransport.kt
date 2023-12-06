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
    fromTransportCommon(PlacedCardCommand.CREATE_PLACED_CARD, request.debug, request)
    fromTransportResource(request.placedCard) {
        PlacedCard(
            ownerId = ownerId.toUserId(),
            box = box.toFcBox(),
            cardId = cardId.toCardId(),
        )
    }
}

fun PlacedCardContext.fromPlacedCardDeleteRequest(request: PlacedCardDeleteRequest) {
    fromTransportCommon(PlacedCardCommand.DELETE_PLACED_CARD, request.debug, request)
    fromTransportResource(request.placedCard) {
        PlacedCard(
            id = id.toPlacedCardId(),
        )
    }
}

fun PlacedCardContext.fromPlacedCardMoveRequest(request: PlacedCardMoveRequest) {
    fromTransportCommon(PlacedCardCommand.MOVE_PLACED_CARD, request.debug, request)
    fromTransportResource(request.move) {
        PlacedCard(
            id = id.toPlacedCardId(),
            box = box.toFcBox(),
        )
    }
}

fun PlacedCardContext.fromPlacedCardInitRequest(request: PlacedCardInitRequest) {
    fromTransportCommon(PlacedCardCommand.INIT_PLACED_CARD, request.debug, request)
    requestOwnerId = request.init?.ownerId.toUserId()
    requestWorkBox = request.init?.box.toFcBox()
}

fun PlacedCardContext.fromPlacedCardSelectRequest(request: PlacedCardSelectRequest) {
    fromTransportCommon(PlacedCardCommand.SELECT_PLACED_CARD, request.debug, request)
    requestOwnerId = request.select?.ownerId.toUserId()
    requestWorkBox = request.select?.box.toFcBox()
    requestSearchStrategy = request.select?.searchStrategy.toFcSearchStrategy()
}

fun PlacedCardContext.fromTransportCommon(cmd: PlacedCardCommand, debug: DebugResource?, request: IRequest) {
    command = cmd
    workMode = debug.transportToWorkMode()
    stubCase = debug.transportToStubCase()
    requestId = request.requestId()
}

private fun <T> PlacedCardContext.fromTransportResource(card: T?, toInternal: T.() -> PlacedCard) {
    requestPlacedCard = card?.toInternal() ?: PlacedCard.EMPTY
}

private fun String?.toPlacedCardId() = this?.let { PlacedCardId(it) } ?: PlacedCardId.NONE
private fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE
private fun String?.toCardId(): CardId = this?.let { CardId(it) } ?: CardId.NONE

private fun Box?.toFcBox(): FcBox = when (this) {
    Box.NEW -> FcBox.NEW
    Box.REPEAT -> FcBox.REPEAT
    Box.FINISHED -> FcBox.FINISHED
    null -> FcBox.NONE
}

private fun SearchStrategy?.toFcSearchStrategy(): FcSearchStrategy = when (this) {
    SearchStrategy.EARLIEST_CREATED -> FcSearchStrategy.EARLIEST_CREATED
    SearchStrategy.EARLIEST_REVIEWED -> FcSearchStrategy.EARLIEST_REVIEWED
    null -> FcSearchStrategy.NONE
}


