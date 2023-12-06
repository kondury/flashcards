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
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    placedCard = toPlacedCardLogModel(),
    errors = errors.map { it.toLog() },
)

private fun PlacedCardContext.toPlacedCardLogModel(): PlacedCardLogModel? =
    PlacedCardLogModel(
        operation = command.toLog(),
        requestPlacedCard = requestPlacedCard.takeNonEmptyOrNull()?.toLog(),
        responsePlacedCard = responsePlacedCard.takeNonEmptyOrNull()?.toLog(),
        requestOwnerId = requestOwnerId.takeNonEmptyOrNull()?.asString(),
        requestWorkBox = requestWorkBox.takeNonEmptyOrNull()?.name,
        requestSearchStrategy = requestSearchStrategy.takeNonEmptyOrNull()?.name
    ).takeIf { it != PlacedCardLogModel() }

private fun PlacedCardCommand.toLog(): Operation? = when (this) {
    PlacedCardCommand.CREATE_PLACED_CARD -> Operation.CREATE
    PlacedCardCommand.MOVE_PLACED_CARD -> Operation.MOVE
    PlacedCardCommand.DELETE_PLACED_CARD -> Operation.DELETE
    PlacedCardCommand.SELECT_PLACED_CARD -> Operation.SELECT
    PlacedCardCommand.INIT_PLACED_CARD -> Operation.INIT
    PlacedCardCommand.NONE -> null
}

private fun FcError.toLog() = ErrorLogModel(
    message = message.takeNonBlankOrNull(),
    field = field.takeNonBlankOrNull(),
    code = code.takeNonBlankOrNull(),
    level = level.name,
)

private fun PlacedCard.toLog() = PlacedCardLog(
    id = id.takeNonEmptyOrNull()?.asString(),
    ownerId = ownerId.takeNonEmptyOrNull()?.asString(),
    box = box.takeNonEmptyOrNull()?.name,
    cardId = cardId.takeNonEmptyOrNull()?.asString(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)

private fun PlacedCard.takeNonEmptyOrNull() = this.takeIf { it.isNotEmpty() }
private fun FcSearchStrategy.takeNonEmptyOrNull() = this.takeIf { it.isNotEmpty() }
private fun FcBox.takeNonEmptyOrNull() = this.takeIf { it.isNotEmpty() }