package com.github.kondury.flashcards.placedcards.stubs

import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant

object PlacedCardStubSample {
    private const val CREATED_ON = "2023-09-09T09:09:09Z"
    private const val UPDATED_ON = "2023-10-10T10:10:10Z"

    val PLACED_CARD_KOTLIN: PlacedCard
        get() = PlacedCard(
            id = PlacedCardId("1000"),
            ownerId = UserId("user-1"),
            box = FcBox.NEW,
            cardId = CardId("222"),
            createdOn = Instant.parse(CREATED_ON),
            updatedOn = Instant.parse(UPDATED_ON),
        )
}
