package com.github.kondury.flashcards.mappers.v1

import com.github.kondury.flashcards.common.models.CardId

internal fun String?.toCardId(): CardId = this?.let { CardId(it) } ?: CardId.NONE

