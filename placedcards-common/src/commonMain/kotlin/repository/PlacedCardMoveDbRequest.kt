package com.github.kondury.flashcards.placedcards.common.repository

import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId

data class PlacedCardMoveDbRequest(
    val id: PlacedCardId,
    val lock: FcPlacedCardLock = FcPlacedCardLock.NONE,
    val box: FcBox = FcBox.NONE,
) {
    constructor(placedCard: PlacedCard) : this(
        id = placedCard.id,
        lock = placedCard.lock,
        box = placedCard.box,
    )
}
