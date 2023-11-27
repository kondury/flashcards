package com.github.kondury.flashcards.cards.repository.inmemory.model

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId

data class CardEntity(
    val id: String? = null,
    val front: String? = null,
    val back: String? = null
) {
    constructor(model: Card) : this(
        id = model.id.asString().takeNonBlankOrNull(),
        front = model.front.takeNonBlankOrNull(),
        back = model.back.takeNonBlankOrNull()
    )

    fun toInternal() = Card(
        id = id?.let { CardId(it) } ?: CardId.NONE,
        front = front ?: "",
        back = back ?: "",
    )
}

private fun String.takeNonBlankOrNull() = this.takeIf { it.isNotBlank() }