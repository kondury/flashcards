package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.common.models.*

internal fun FcRequestId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }
internal fun UserId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }
internal fun PlacedCardId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }
internal fun CardId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }

internal fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }
