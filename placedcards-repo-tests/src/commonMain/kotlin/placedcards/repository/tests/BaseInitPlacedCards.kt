package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.NONE
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant

abstract class BaseInitPlacedCards(private val command: String) : InitObjects<PlacedCard> {

    private val stubLock: FcPlacedCardLock = FcPlacedCardLock("stubLock")

    fun createInitTestModel(
        suffix: String,
        ownerId: UserId = UserId("owner-123"),
        box: FcBox = FcBox.NEW,
        cardId: CardId = CardId("card-repo-$command-$suffix"),
        createdAt: Instant = Instant.NONE,
        updatedAt: Instant = Instant.NONE,
        lock: FcPlacedCardLock = stubLock,
    ) = PlacedCard(
        id = PlacedCardId("placedCard-repo-$command-$suffix"),
        ownerId = ownerId,
        box = box,
        cardId = cardId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lock = lock,
    )
}
