package com.github.kondury.flashcards.placedcards.common.helpers

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcError

fun Throwable.asFcError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message.orEmpty(),
) = FcError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun PlacedCardContext.addError(vararg error: FcError) = errors.addAll(error)