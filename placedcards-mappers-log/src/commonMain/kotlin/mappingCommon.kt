package com.github.kondury.flashcards.placedcards.api.logs.mapper

import com.github.kondury.flashcards.placedcards.common.models.FcRequestId

internal fun FcRequestId.asStringOrNull() = this.asString().takeNonBlankOrNull()

internal fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }
