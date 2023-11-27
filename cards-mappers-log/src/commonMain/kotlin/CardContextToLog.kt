package com.github.kondury.flashcards.cards.api.logs.mapper

import com.github.kondury.flashcards.cards.api.logs.models.CardLog
import com.github.kondury.flashcards.cards.api.logs.models.CardLogModel
import com.github.kondury.flashcards.cards.api.logs.models.CardLogModel.Operation
import com.github.kondury.flashcards.cards.api.logs.models.ErrorLogModel
import com.github.kondury.flashcards.cards.api.logs.models.LogModel
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlinx.datetime.Clock

fun CardContext.toLog(logId: String) = LogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "flashcards-cards",
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    card = toCardLogModel(),
    errors = errors.map { it.toLog() },
)

private fun CardContext.toCardLogModel(): CardLogModel? {
    return CardLogModel(
        operation = command.toLog(),
        requestCard = requestCard.takeNonEmptyOrNull()?.toLog(),
        responseCard = responseCard.takeNonEmptyOrNull()?.toLog(),
    ).takeIf { it != CardLogModel() }
}

private fun CardCommand.toLog(): Operation? = when (this) {
    CardCommand.CREATE_CARD -> Operation.CREATE
    CardCommand.READ_CARD -> Operation.READ
    CardCommand.DELETE_CARD -> Operation.DELETE
    CardCommand.NONE -> null
}

private fun FcError.toLog() = ErrorLogModel(
    message = message.takeNonBlankOrNull(),
    field = field.takeNonBlankOrNull(),
    code = code.takeNonBlankOrNull(),
    level = level.name,
)

private fun Card.toLog() = CardLog(
    id = id.takeNonEmptyOrNull()?.asString(),
    front = front.takeNonBlankOrNull(),
    back = back.takeNonBlankOrNull(),
)

private fun Card.takeNonEmptyOrNull(): Card? = this.takeIf { it.isNotEmpty() }
