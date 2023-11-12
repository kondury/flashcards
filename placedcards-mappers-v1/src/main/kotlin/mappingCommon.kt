package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.common.models.*

internal inline fun FcRequestId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }
internal inline fun UserId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }
internal inline fun PlacedCardId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }
internal inline fun CardId.takeNonEmptyOrNull() = takeIf { it.isNotEmpty() }

internal inline fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }
