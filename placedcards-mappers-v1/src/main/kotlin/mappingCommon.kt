package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.common.models.*

internal fun PlacedCardId.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun UserId.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun CardId.asStringOrNull() = this.asString().takeNonBlankOrNull()
internal fun FcPlacedCardLock.asStringOrNull() = this.asString().takeNonBlankOrNull()

internal fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }

internal fun <T, U> T?.mapOrDefault(defaultValue: U, mapper: (T) -> U) = this?.let(mapper) ?: defaultValue
