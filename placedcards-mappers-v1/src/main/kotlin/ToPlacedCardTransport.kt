package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.mappers.v1.exceptions.UnknownFcCommand

fun PlacedCardContext.toTransportPlacedCard(): IResponse = when (val cmd = command) {
    PlacedCardCommand.CREATE_PLACED_CARD -> toPlacedCardCreateResponse()
    PlacedCardCommand.MOVE_PLACED_CARD -> toPlacedCardMoveResponse()
    PlacedCardCommand.DELETE_PLACED_CARD -> toPlacedCardDeleteResponse()
    PlacedCardCommand.SELECT_PLACED_CARD -> toPlacedCardSelectResponse()
    PlacedCardCommand.INIT_PLACED_CARD -> toPlacedCardInitResponse()
    PlacedCardCommand.NONE -> throw UnknownFcCommand(cmd)
}

fun PlacedCardContext.toPlacedCardCreateResponse() = PlacedCardCreateResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    placedCard = responsePlacedCard.toPlacedCardResponseResource(),
)

fun PlacedCardContext.toPlacedCardMoveResponse() = PlacedCardMoveResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    placedCard = responsePlacedCard.toPlacedCardResponseResource(),
)

fun PlacedCardContext.toPlacedCardDeleteResponse() = PlacedCardDeleteResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun PlacedCardContext.toPlacedCardSelectResponse() = PlacedCardSelectResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    placedCard = responsePlacedCard.toPlacedCardResponseResource(),
)

fun PlacedCardContext.toPlacedCardInitResponse() = PlacedCardInitResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun PlacedCard.toPlacedCardResponseResource() = PlacedCardResponseResource(
    id = id.takeNonEmptyOrNull()?.asString(),
    ownerId = ownerId.takeNonEmptyOrNull()?.asString(),
    cardId = cardId.takeNonEmptyOrNull()?.asString(),
    box = box.toTransportPlacedCard(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)

fun FcBox.toTransportPlacedCard(): Box? = when (this) {
    FcBox.NEW -> Box.NEW
    FcBox.REPEAT -> Box.REPEAT
    FcBox.FINISHED -> Box.FINISHED
    FcBox.NONE -> null
}
