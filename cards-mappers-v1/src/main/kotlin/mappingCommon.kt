package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock

internal fun CardId.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun FcCardLock.asStringOrNull() = this.asString().takeNonBlankOrNull()

internal fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }

internal fun <T, U> T?.mapOrDefault(defaultValue: U, mapper: (T) -> U) = this?.let(mapper) ?: defaultValue
