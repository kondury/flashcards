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
        createdAt: Instant = STUB.createdAt,
        updatedAt: Instant = STUB.updatedAt,
    ): PlacedCard = STUB.copy(
        id = id,
        ownerId = ownerId,
        box = box,
        cardId = cardId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
