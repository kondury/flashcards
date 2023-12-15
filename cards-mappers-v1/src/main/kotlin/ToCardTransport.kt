package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand.*
import com.github.kondury.flashcards.cards.mappers.v1.exceptions.UnknownFcCommand


fun CardContext.toTransportCard(): IResponse = when (val cmd = command) {
    CREATE_CARD -> {
        CardCreateResponse(null, toRequestId(), toResult(), toErrors(), toCard())
    }
    READ_CARD -> {
        CardReadResponse(null, toRequestId(), toResult(), toErrors(), toCard())
    }
    DELETE_CARD -> {
        CardDeleteResponse(null, toRequestId(), toResult(), toErrors())
    }
    NONE -> {
        throw UnknownFcCommand(cmd)
    }
}

private fun CardContext.toRequestId() = requestId.asStringOrNull()
private fun CardContext.toResult() = state.toResponseResultOrNull()
private fun CardContext.toErrors() = errors.toTransportErrorsOrNull()
private fun CardContext.toCard() = responseCard.toCardResponseResource()

private fun Card.toCardResponseResource() = CardResponseResource(
    id = id.asStringOrNull(),
    front = front.takeNonBlankOrNull(),
    back = back.takeNonBlankOrNull(),
)

