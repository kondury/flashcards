package com.github.kondury.flashcards.common.models

import com.github.kondury.flashcards.common.NONE
import kotlinx.datetime.Instant

data class PlacedCard(
    val id: PlacedCardId = PlacedCardId.NONE,
    val ownerId: UserId = UserId.NONE,
    val box: Box = Box.NONE,
    val cardId: CardId = CardId.NONE,
    val createdOn: Instant = Instant.NONE,
    val updatedOn: Instant = Instant.NONE
)

