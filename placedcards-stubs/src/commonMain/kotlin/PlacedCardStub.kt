package com.github.kondury.flashcards.placedcards.stubs

import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStubSample.PLACED_CARD_KOTLIN as STUB

object PlacedCardStub {
    fun get(): PlacedCard = STUB.copy()

    fun getWith(
        id: PlacedCardId = STUB.id,
        ownerId: UserId = STUB.ownerId,
        box: FcBox = STUB.box,
        cardId: CardId = STUB.cardId,
        createdOn: Instant = STUB.createdOn,
        updatedOn: Instant = STUB.updatedOn,
    ): PlacedCard = STUB.copy(
        id = id,
        ownerId = ownerId,
        box = box,
        cardId = cardId,
        createdOn = createdOn,
        updatedOn = updatedOn
    )


//    fun prepareResult(block: PlacedCard.() -> Unit): PlacedCard = get().apply(block)
//
//    private fun fcCard(
//        base: PlacedCard,
//        id: String,
//        box: FcBox,
//        cardId: String,
//    ) = base.copy(
//        id = PlacedCardId(id),
//        box = box,
//        cardId = CardId(cardId),
//    )

}
