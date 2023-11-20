package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.addError
import com.github.kondury.flashcards.placedcards.common.models.FcError
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*

fun PlacedCardContext.fail(error: FcError) {
    state = FcState.FAILING
    addError(error)
}

internal fun validationError(
    command: PlacedCardCommand = NONE,
    workMode: FcWorkMode = FcWorkMode.PROD,
    field: String,
    violationCode: String,
    description: String,
    level: FcError.Level = FcError.Level.INFO,
): FcError {
    val group = if (workMode == FcWorkMode.PROD) "validation"
    else "validation-${workMode.name.lowercase()}"

    return FcError(
        code = "validation-${command.name.lowercase()}-$field-$violationCode",
        field = field,
        group = group,
        message = "Validation error for field $field: $description",
        level = level,
    )
}
