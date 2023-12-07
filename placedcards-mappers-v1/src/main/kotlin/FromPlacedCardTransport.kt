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
    requestPlacedCard = request.placedCard.mapOrDefault(PlacedCard.EMPTY) {
        PlacedCard(
            ownerId = it.ownerId.mapOrDefault(UserId.NONE, ::UserId),
            box = it.box.toFcBoxOrNone(),
            cardId = it.cardId.mapOrDefault(CardId.NONE, ::CardId),
        )
    }
}

fun PlacedCardContext.fromPlacedCardDeleteRequest(request: PlacedCardDeleteRequest) {
    fromTransportCommon(PlacedCardCommand.DELETE_PLACED_CARD, request.debug, request)
    requestPlacedCard = request.placedCard.mapOrDefault(PlacedCard.EMPTY) {
        PlacedCard(
            id = it.id.mapOrDefault(PlacedCardId.NONE, ::PlacedCardId),
            lock = it.lock.mapOrDefault(FcPlacedCardLock.NONE, ::FcPlacedCardLock),
        )
    }
}

fun PlacedCardContext.fromPlacedCardMoveRequest(request: PlacedCardMoveRequest) {
    fromTransportCommon(PlacedCardCommand.MOVE_PLACED_CARD, request.debug, request)
    requestPlacedCard = request.move.mapOrDefault(PlacedCard.EMPTY) {
        PlacedCard(
            id = it.id.mapOrDefault(PlacedCardId.NONE, ::PlacedCardId),
            box = it.box.toFcBoxOrNone(),
            lock = it.lock.mapOrDefault(FcPlacedCardLock.NONE, ::FcPlacedCardLock),
        )
    }
}

fun PlacedCardContext.fromPlacedCardInitRequest(request: PlacedCardInitRequest) {
    fromTransportCommon(PlacedCardCommand.INIT_PLACED_CARD, request.debug, request)
    requestOwnerId = request.init?.ownerId.mapOrDefault(UserId.NONE, ::UserId)
    requestWorkBox = request.init?.box.toFcBoxOrNone()
}

fun PlacedCardContext.fromPlacedCardSelectRequest(request: PlacedCardSelectRequest) {
    fromTransportCommon(PlacedCardCommand.SELECT_PLACED_CARD, request.debug, request)
    requestOwnerId = request.select?.ownerId.mapOrDefault(UserId.NONE, ::UserId)
    requestWorkBox = request.select?.box.toFcBoxOrNone()
    requestSearchStrategy = request.select?.searchStrategy.toFcSearchStrategyOrNone()
}

private fun PlacedCardContext.fromTransportCommon(cmd: PlacedCardCommand, debug: DebugResource?, request: IRequest) {
    command = cmd
    workMode = debug.toWorkMode()
    stubCase = debug.toStubCaseOrNone()
    requestId = request.requestId.mapOrDefault(FcRequestId.NONE, ::FcRequestId)
}

private fun Box?.toFcBoxOrNone() = when (this) {
    Box.NEW -> FcBox.NEW
    Box.REPEAT -> FcBox.REPEAT
    Box.FINISHED -> FcBox.FINISHED
    null -> FcBox.NONE
}

private fun SearchStrategy?.toFcSearchStrategyOrNone() = when (this) {
    SearchStrategy.EARLIEST_CREATED -> FcSearchStrategy.EARLIEST_CREATED
    SearchStrategy.EARLIEST_REVIEWED -> FcSearchStrategy.EARLIEST_REVIEWED
    null -> FcSearchStrategy.NONE
}
