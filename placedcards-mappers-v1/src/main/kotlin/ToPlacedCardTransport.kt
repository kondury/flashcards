package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*
import com.github.kondury.flashcards.placedcards.mappers.v1.exceptions.UnknownFcCommand

fun PlacedCardContext.toTransportPlacedCard(): IResponse = when (val cmd = command) {
    CREATE_PLACED_CARD -> {
        PlacedCardCreateResponse(null, toRequestId(), toResult(), toErrors(), toPlacedCard())
    }
    MOVE_PLACED_CARD -> {
        PlacedCardMoveResponse(null, toRequestId(), toResult(), toErrors(), toPlacedCard())
    }
    DELETE_PLACED_CARD -> {
        PlacedCardDeleteResponse(null, toRequestId(), toResult(), toErrors())
    }
    SELECT_PLACED_CARD -> {
        PlacedCardSelectResponse(null, toRequestId(), toResult(), toErrors(), toPlacedCard())
    }
    INIT_PLACED_CARD -> {
        PlacedCardInitResponse(null, toRequestId(), toResult(), toErrors())
    }
    NONE -> {
        throw UnknownFcCommand(cmd)
    }
}

private fun PlacedCardContext.toRequestId() = requestId.asStringOrNull()
private fun PlacedCardContext.toResult() = state.toResponseResultOrNull()
private fun PlacedCardContext.toErrors() = errors.toTransportErrorsOrNull()
private fun PlacedCardContext.toPlacedCard() = responsePlacedCard.toPlacedCardResponseResource()

fun PlacedCard.toPlacedCardResponseResource() = PlacedCardResponseResource(
    id = id.asStringOrNull(),
    ownerId = ownerId.asStringOrNull(),
    cardId = cardId.asStringOrNull(),
    box = box.toTransportBoxOrNull(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)

fun FcBox.toTransportBoxOrNull(): Box? = when (this) {
    FcBox.NEW -> Box.NEW
    FcBox.REPEAT -> Box.REPEAT
    FcBox.FINISHED -> Box.FINISHED
    FcBox.NONE -> null
}
