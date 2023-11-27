package com.github.kondury.flashcards.cards.common

import com.github.kondury.flashcards.cards.common.repository.CardRepository


class CardRepositoryConfig(
    val testRepository: CardRepository = CardRepository.NoOpCardRepository,
    val prodRepository: CardRepository = CardRepository.NoOpCardRepository,
)  {
    companion object {
        val NONE = CardRepositoryConfig()
    }
}