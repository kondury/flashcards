package com.github.kondury.flashcards.mappers.v1

import com.github.kondury.flashcards.api.v1.models.*
import com.github.kondury.flashcards.common.CardContext
import com.github.kondury.flashcards.common.models.*
import com.github.kondury.flashcards.mappers.v1.exceptions.UnknownFcCommand

fun CardContext.toTransportCard(): IResponse = when (val cmd = command) {
    CardCommand.CREATE_CARD -> toCardCreateResponse()
    CardCommand.READ_CARD -> toCardReadResponse()
    CardCommand.DELETE_CARD -> toCardDeleteResponse()
    CardCommand.NONE -> throw UnknownFcCommand(cmd)
}

fun CardContext.toCardCreateResponse() = CardCreateResponse(
    responseType = "createCard",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    card = cardResponse.toCardResponseResource(),
)

fun CardContext.toCardReadResponse() = CardReadResponse(
    responseType = "readCard",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    card = cardResponse.toCardResponseResource(),
)

fun CardContext.toCardDeleteResponse() = CardDeleteResponse(
    responseType = "deleteCard",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

private fun Card.toCardResponseResource() = CardResponseResource(
    id = id.takeIf { it != CardId.NONE }?.asString(),
    front = front.takeIf { it.isNotBlank() },
    back = back.takeIf { it.isNotBlank() },
)

