package com.github.kondury.flashcards.placedcards.repository.inmemory

import com.github.kondury.flashcards.placedcards.repository.tests.RepoCreatePlacedCardContract
import com.github.kondury.flashcards.placedcards.repository.tests.RepoDeletePlacedCardContract
import com.github.kondury.flashcards.placedcards.repository.tests.RepoMovePlacedCardContract
import com.github.kondury.flashcards.placedcards.repository.tests.RepoSelectPlacedCardContract

@Suppress("unused")
class InMemoryPlacedCardRepositoryCreateTest : RepoCreatePlacedCardContract {
    override val placedCardRepository = InMemoryPlacedCardRepository(
        initObjects = RepoCreatePlacedCardContract.initObjects,
        randomUuid = this::uuid
    )
}

@Suppress("unused")
class InMemoryPlacedCardRepositoryDeleteTest : RepoDeletePlacedCardContract {
    override val placedCardRepository = InMemoryPlacedCardRepository(
        initObjects = RepoDeletePlacedCardContract.initObjects
    )
}

@Suppress("unused")
class InMemoryPlacedCardRepositoryMoveTest : RepoMovePlacedCardContract {
    override val placedCardRepository = InMemoryPlacedCardRepository(
        initObjects = RepoMovePlacedCardContract.initObjects,
        randomUuid = this::uuid
    )
}

@Suppress("unused")
class InMemoryPlacedCardRepositorySelectTest : RepoSelectPlacedCardContract {
    override val placedCardRepository = InMemoryPlacedCardRepository(
        initObjects = RepoSelectPlacedCardContract.initObjects
    )
}