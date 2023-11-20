package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.helpers.addError
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcState


fun CardContext.fail(error: FcError) {
    state = FcState.FAILING
    addError(error)
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