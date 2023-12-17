package com.github.kondury.flashcards.cards.repository.inmemory.model

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock

data class CardEntity(
    val id: String? = null,
    val front: String? = null,
    val back: String? = null,
    val lock: String? = null,
) {
    constructor(model: Card) : this(
        id = model.id.asString().takeNonBlankOrNull(),
        lock = model.lock.asString().takeNonBlankOrNull(),
        front = model.front.takeNonBlankOrNull(),
        back = model.back.takeNonBlankOrNull(),
    )

    fun toInternal() = Card(
        lock = lock?.let { FcCardLock(it) } ?: FcCardLock.NONE,
        id = id?.let { CardId(it) } ?: CardId.NONE,
        front = front.orEmpty(),
        back = back.orEmpty(),
    )
}

private fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }