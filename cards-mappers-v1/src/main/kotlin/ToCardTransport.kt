package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.mappers.v1.exceptions.UnknownFcCommand

fun CardContext.toTransportCard(): IResponse = when (val cmd = command) {
    CardCommand.CREATE_CARD -> toCardCreateResponse()
    CardCommand.READ_CARD -> toCardReadResponse()
    CardCommand.DELETE_CARD -> toCardDeleteResponse()
    CardCommand.NONE -> throw UnknownFcCommand(cmd)
}

fun CardContext.toCardCreateResponse() = CardCreateResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    card = responseCard.toCardResponseResource(),
)

fun CardContext.toCardReadResponse() = CardReadResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    card = responseCard.toCardResponseResource(),
)

fun CardContext.toCardDeleteResponse() = CardDeleteResponse(
    requestId = requestId.takeNonEmptyOrNull()?.asString(),
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

private fun Card.toCardResponseResource() = CardResponseResource(
    id = id.takeNonEmptyOrNull()?.asString(),
    front = front.takeNonBlankOrNull(),
    back = back.takeNonBlankOrNull(),
)

