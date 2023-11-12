package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcState


internal fun FcState.toResult(): ResponseResult? = when (this) {
    FcState.RUNNING -> ResponseResult.SUCCESS
    FcState.FAILING -> ResponseResult.ERROR
    FcState.NONE -> null
}

internal fun List<FcError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun FcError.toTransportError() = Error(
    code = code.takeNonBlankOrNull(),
    group = group.takeNonBlankOrNull(),
    field = field.takeNonBlankOrNull(),
    message = message.takeNonBlankOrNull(),
)