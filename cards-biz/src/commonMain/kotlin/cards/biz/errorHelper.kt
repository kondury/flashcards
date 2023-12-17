package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.helpers.addError
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcState


fun CardContext.fail(error: FcError) {
    state = FcState.FAILING
    addError(error)
}

fun CardContext.fail(errors: List<FcError>) {
    state = FcState.FAILING
    addError(*errors.toTypedArray())
}

internal fun validationError(
    field: String,
    violationCode: String,
    description: String,
    level: FcError.Level = FcError.Level.INFO,
) = FcError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

internal fun configurationError(
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: FcError.Level = FcError.Level.ERROR,
) = FcError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)
