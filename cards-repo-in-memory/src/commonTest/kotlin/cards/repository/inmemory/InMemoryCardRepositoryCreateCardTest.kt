package com.github.kondury.flashcards.cards.repository.inmemory

import com.github.kondury.flashcards.cards.repository.tests.RepoCreateCardContract
import com.github.kondury.flashcards.cards.repository.tests.RepoCreateCardContract.Companion.initObjects

@Suppress("unused")
class InMemoryCardRepositoryCreateCardTest : RepoCreateCardContract {
    override val cardRepository = InMemoryCardRepository(
        initObjects = initObjects,
        randomUuid = this::uuid
    )
}