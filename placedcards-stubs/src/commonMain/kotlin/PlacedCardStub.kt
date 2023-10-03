package com.github.kondury.flashcards.placedcards.stubs

import com.github.kondury.flashcards.placedcards.common.models.CardId
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId

object PlacedCardStub {
    fun get(): PlacedCard = PlacedCardStubSample.PLACED_CARD_KOTLIN.copy()

    fun prepareResult(block: PlacedCard.() -> Unit): PlacedCard = get().apply(block)

    private fun fcCard(
        base: PlacedCard,
        id: String,
        box: FcBox,
        cardId: String,
    ) = base.copy(
        id = PlacedCardId(id),
        box = box,
        cardId = CardId(cardId),
    )

}
