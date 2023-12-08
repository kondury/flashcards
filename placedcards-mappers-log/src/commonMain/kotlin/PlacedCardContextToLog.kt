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
    requestId = requestId.asStringOrNull(),
    placedCard = toPlacedCardLogModel(),
    errors = errors.map { it.toLog() },
)

private fun PlacedCardContext.toPlacedCardLogModel(): PlacedCardLogModel? =
    PlacedCardLogModel(
        operation = command.toLog(),
        requestPlacedCard = requestPlacedCard.takeNonEmptyOrNull()?.toLog(),
        responsePlacedCard = responsePlacedCard.takeNonEmptyOrNull()?.toLog(),
        requestOwnerId = requestOwnerId.asStringOrNull(),
        requestWorkBox = requestWorkBox.asStringOrNull(),
        requestSearchStrategy = requestSearchStrategy.asStringOrNull()
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
    id = id.asStringOrNull(),
    ownerId = ownerId.asStringOrNull(),
    box = box.asStringOrNull(),
    cardId = cardId.asStringOrNull(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)

private fun PlacedCard.takeNonEmptyOrNull() = this.takeIf { it.isNotEmpty() }

private fun PlacedCardId.asStringOrNull() = this.asString().takeNonBlankOrNull()
private fun UserId.asStringOrNull() = this.asString().takeNonBlankOrNull()
private fun CardId.asStringOrNull() = this.asString().takeNonBlankOrNull()

private fun FcSearchStrategy.asStringOrNull() = this.takeIf { it.isNotEmpty() }?.name
private fun FcBox.asStringOrNull() = this.takeIf { it.isNotEmpty() }?.name