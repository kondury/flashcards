package com.github.kondury.flashcards.placedcards.common.repository

import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId

data class PlacedCardIdDbRequest(
    val id: PlacedCardId,
    val lock: FcPlacedCardLock = FcPlacedCardLock.NONE,
) {
    constructor(placedCard: PlacedCard) : this(placedCard.id, placedCard.lock)
}