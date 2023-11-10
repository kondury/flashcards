package com.github.kondury.flashcards.placedcards.common.models

import com.github.kondury.flashcards.placedcards.common.NONE
import kotlinx.datetime.Instant

data class PlacedCard(
    val id: PlacedCardId = PlacedCardId.NONE,
    val ownerId: UserId = UserId.NONE,
    val box: FcBox = FcBox.NONE,
    val cardId: CardId = CardId.NONE,
    val createdOn: Instant = Instant.NONE,
    val updatedOn: Instant = Instant.NONE
) {
    companion object {
        val EMPTY = PlacedCard()
    }
}

fun PlacedCard.isEmpty() = this == PlacedCard.EMPTY
fun PlacedCard.isNotEmpty() = this != PlacedCard.EMPTY
