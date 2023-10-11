package com.github.kondury.flashcards.cards.common.helpers

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcError

fun Throwable.asFcError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = FcError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun CardContext.addError(vararg error: FcError) = errors.addAll(error)
