package com.github.kondury.flashcards.placedcards.common

import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository


class PlacedCardRepositoryConfig(
    val testRepository: PlacedCardRepository = PlacedCardRepository.NoOpPlacedCardRepository,
    val prodRepository: PlacedCardRepository = PlacedCardRepository.NoOpPlacedCardRepository,
)  {
    companion object {
        val NONE = PlacedCardRepositoryConfig()
    }
}