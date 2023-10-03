package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.common.models.CardId

internal fun String?.toCardId(): CardId = this?.let { CardId(it) } ?: CardId.NONE

