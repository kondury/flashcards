package com.github.kondury.flashcards.placedcards.api.logs.mapper

import com.github.kondury.flashcards.placedcards.api.logs.models.ErrorLogModel
import com.github.kondury.flashcards.placedcards.api.logs.models.LogModel
import com.github.kondury.flashcards.placedcards.api.logs.models.PlacedCardLog
import com.github.kondury.flashcards.placedcards.api.logs.models.PlacedCardLogModel
import com.github.kondury.flashcards.placedcards.api.logs.models.PlacedCardLogModel.Operation
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Clock

fun PlacedCardContext.toLog(logId: String) = LogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "flashcards-placedcards",
    requestId = requestId.takeIf { it != FcRequestId.NONE }?.asString(),
    placedCard = toPlacedCardLogModel(),
    errors = errors.map { it.toLog() },
)

private fun PlacedCardContext.toPlacedCardLogModel(): PlacedCardLogModel? {
    val placedCardNone = PlacedCard()
    return PlacedCardLogModel(
        operation = command.toLog(),
        requestPlacedCard = requestPlacedCard.takeIf { it != placedCardNone }?.toLog(),
        responsePlacedCard = responsePlacedCard.takeIf { it != placedCardNone }?.toLog(),
        requestOwnerId = requestOwnerId.takeIf { it != UserId.NONE }?.asString(),
        requestWorkBox = requestWorkBox.takeIf { it != FcBox.NONE }?.name,
        requestBoxAfter = requestBoxAfter.takeIf { it != FcBox.NONE }?.name,
        requestSearchStrategy = requestSearchStrategy.takeIf { it != FcSearchStrategy.NONE }?.name
    ).takeIf { it != PlacedCardLogModel() }
}

private fun PlacedCardCommand.toLog(): Operation? = when (this) {
    PlacedCardCommand.CREATE_PLACED_CARD -> Operation.CREATE
    PlacedCardCommand.MOVE_PLACED_CARD -> Operation.MOVE
    PlacedCardCommand.DELETE_PLACED_CARD -> Operation.DELETE
    PlacedCardCommand.SELECT_PLACED_CARD -> Operation.SELECT
    PlacedCardCommand.INIT_PLACED_CARD -> Operation.INIT
    PlacedCardCommand.NONE -> null
}

private fun FcError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun PlacedCard.toLog() = PlacedCardLog(
    id = id.takeIf { it != PlacedCardId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    box = box.takeIf { it != FcBox.NONE }?.name,
    cardId = cardId.takeIf { it != CardId.NONE }?.asString(),
    createdOn = createdOn.toString(),
    updatedOn = updatedOn.toString(),
)
