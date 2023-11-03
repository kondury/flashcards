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
    requestId = requestId.takeIf { it != FcRequestId.NONE }?.asString(),
    card = toCardLogModel(),
    errors = errors.map { it.toLog() },
)

private fun CardContext.toCardLogModel(): CardLogModel? {
    val cardNone = Card()
    return CardLogModel(
        operation = command.toLog(),
        requestCard = requestCard.takeIf { it != cardNone }?.toLog(),
        responseCard = responseCard.takeIf { it != cardNone }?.toLog(),
    ).takeIf { it != CardLogModel() }
}

private fun CardCommand.toLog(): Operation? = when (this) {
    CardCommand.CREATE_CARD -> Operation.CREATE
    CardCommand.READ_CARD -> Operation.READ
    CardCommand.DELETE_CARD -> Operation.DELETE
    CardCommand.NONE -> null
}

private fun FcError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Card.toLog() = CardLog(
    id = id.takeIf { it != CardId.NONE }?.asString(),
    front = front.takeIf { it.isNotBlank() },
    back = back.takeIf { it.isNotBlank() },
)