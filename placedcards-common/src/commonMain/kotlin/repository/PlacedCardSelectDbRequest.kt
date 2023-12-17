package com.github.kondury.flashcards.placedcards.common.repository

import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy
import com.github.kondury.flashcards.placedcards.common.models.UserId


data class PlacedCardSelectDbRequest(
    val ownerId: UserId,
    val strategy: FcSearchStrategy,
    val box: FcBox
)
