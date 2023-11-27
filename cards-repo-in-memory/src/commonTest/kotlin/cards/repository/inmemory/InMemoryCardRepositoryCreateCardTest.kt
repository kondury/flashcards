package com.github.kondury.flashcards.cards.repository.inmemory

import com.github.kondury.flashcards.cards.repository.tests.RepoCreateCardContract
import com.github.kondury.flashcards.cards.repository.tests.RepoCreateCardContract.Companion.initObjects

// todo remove redundant old code after thorough testing including other repository implementations
//class InMemoryCardRepositoryCreateCardTest : RepoCreateCardTest() {
//    override val repo = InMemoryCardRepository(
//        initObjects = initObjects,
//    )
//}

@Suppress("unused")
class InMemoryCardRepositoryCreateCardTest : RepoCreateCardContract {
    override val cardRepository = InMemoryCardRepository(
        initObjects = initObjects,
    )

}