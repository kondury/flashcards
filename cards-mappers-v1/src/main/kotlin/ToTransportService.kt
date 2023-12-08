package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcRequestId
import com.github.kondury.flashcards.cards.common.models.FcState

internal fun FcRequestId.asStringOrNull() = this.asString().takeNonBlankOrNull()

internal fun FcState.toResponseResultOrNull(): ResponseResult? = when (this) {
    FcState.RUNNING -> ResponseResult.SUCCESS
    FcState.FAILING -> ResponseResult.ERROR
    FcState.FINISHING -> ResponseResult.SUCCESS
    FcState.NONE -> null
}

internal fun List<FcError>.toTransportErrorsOrNull(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun FcError.toTransportError() = Error(
    code = code.takeNonBlankOrNull(),
    group = group.takeNonBlankOrNull(),
    field = field.takeNonBlankOrNull(),
    message = message.takeNonBlankOrNull(),
)