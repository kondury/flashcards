package com.github.kondury.flashcards.placedcards.stubs

import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant

object PlacedCardStubSample {
    private const val CREATED_AT = "2023-09-09T09:09:09Z"
    private const val UPDATED_AT = "2023-10-10T10:10:10Z"

    val PLACED_CARD_KOTLIN: PlacedCard
        get() = PlacedCard(
            id = PlacedCardId("1000"),
            lock = FcPlacedCardLock("stub-lock"),
            ownerId = UserId("user-1"),
            box = FcBox.NEW,
            cardId = CardId("card-id-1"),
            createdAt = Instant.parse(CREATED_AT),
            updatedAt = Instant.parse(UPDATED_AT),
        )
}
