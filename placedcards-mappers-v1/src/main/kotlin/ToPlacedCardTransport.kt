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
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    placedCard = responsePlacedCard.toPlacedCardResponseResource(),
)

fun PlacedCardContext.toPlacedCardMoveResponse() = PlacedCardMoveResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    placedCard = responsePlacedCard.toPlacedCardResponseResource(),
)

fun PlacedCardContext.toPlacedCardDeleteResponse() = PlacedCardDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun PlacedCardContext.toPlacedCardSelectResponse() = PlacedCardSelectResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    placedCard = responsePlacedCard.toPlacedCardResponseResource(),
)

fun PlacedCardContext.toPlacedCardInitResponse() = PlacedCardInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun PlacedCard.toPlacedCardResponseResource() = PlacedCardResponseResource(
    id = id.takeIf { it != PlacedCardId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    cardId = cardId.takeIf { it != CardId.NONE }?.asString(),
    box = box.toTransportPlacedCard(),
    createdOn = createdOn.toString(),
    updatedOn = updatedOn.toString(),
)

private fun FcBox.toTransportPlacedCard(): Box? = when (this) {
    FcBox.NEW -> Box.NEW
    FcBox.REPEAT -> Box.REPEAT
    FcBox.FINISHED -> Box.FINISHED
    FcBox.NONE -> null
}
