package com.github.kondury.flashcards.cards.api.logs.mapper

import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcRequestId
import com.github.kondury.flashcards.cards.common.models.isNotEmpty

internal fun CardId.takeNonEmptyOrNull() = this.takeIf { it.isNotEmpty() }
internal fun FcRequestId.takeNonEmptyOrNull() = this.takeIf { it.isNotEmpty() }
internal fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }
