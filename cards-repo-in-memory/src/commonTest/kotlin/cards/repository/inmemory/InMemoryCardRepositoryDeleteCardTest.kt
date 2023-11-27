package com.github.kondury.flashcards.cards.repository.inmemory

import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.tests.RepoDeleteCardContract
import com.github.kondury.flashcards.cards.repository.tests.RepoDeleteCardContract.Companion.initObjects

// todo remove redundant old code after thorough testing including other repository implementations
//class InMemoryCardRepositoryDeleteCardTest : RepoDeleteCardTest() {
//    override val repo: CardRepository = InMemoryCardRepository(
//        initObjects = initObjects
//    )
//}

@Suppress("unused")
class InMemoryCardRepositoryDeleteCardTest : RepoDeleteCardContract {
    override val cardRepository: CardRepository = InMemoryCardRepository(
        initObjects = initObjects
    )
}