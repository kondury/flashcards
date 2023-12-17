package com.github.kondury.flashcards.cards.common.repository

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock

data class CardIdDbRequest(
    val id: CardId,
    val lock: FcCardLock = FcCardLock.NONE,
) {
    constructor(card: Card) : this(card.id, card.lock)
}