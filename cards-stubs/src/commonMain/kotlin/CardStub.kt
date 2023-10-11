package com.github.kondury.flashcards.cards.stubs

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId

object CardStub {
    fun get(): Card = CardStubSample.CARD_KOTLIN.copy()

    fun prepareResult(block: Card.() -> Unit): Card = get().apply(block)

    private fun fcCard(base: Card, id: String, front: String, back: String) = base.copy(
        id = CardId(id),
        front = "$front $id",
        back = "$back $id"
    )

}
