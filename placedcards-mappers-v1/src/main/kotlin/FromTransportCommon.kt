package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.common.models.CardId

internal fun String?.toCardId(): CardId = this?.let { CardId(it) } ?: CardId.NONE

