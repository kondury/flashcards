package com.github.kondury.flashcards.cards.repository.inmemory

import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.tests.RepoReadCardContract
import com.github.kondury.flashcards.cards.repository.tests.RepoReadCardContract.Companion.initObjects

@Suppress("unused")
class InMemoryCardRepositoryReadCardTest : RepoReadCardContract {
    override val cardRepository: CardRepository = InMemoryCardRepository(
        initObjects = initObjects
    )
}