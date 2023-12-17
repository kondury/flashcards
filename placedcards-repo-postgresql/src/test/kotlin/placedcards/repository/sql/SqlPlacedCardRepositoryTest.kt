package com.github.kondury.flashcards.placedcards.repository.sql

import com.github.kondury.flashcards.placedcards.repository.tests.RepoCreatePlacedCardContract
import com.github.kondury.flashcards.placedcards.repository.tests.RepoDeletePlacedCardContract
import com.github.kondury.flashcards.placedcards.repository.tests.RepoMovePlacedCardContract
import com.github.kondury.flashcards.placedcards.repository.tests.RepoSelectPlacedCardContract
import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

@Suppress("unused")
class SqlPlacedCardRepositoryCreateTest : RepoCreatePlacedCardContract {
    override val placedCardRepository = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        RepoCreatePlacedCardContract.initObjects,
        randomUuid = this::uuid,
    )
}

@Suppress("unused")
class SqlPlacedCardRepositoryDeleteTest : RepoDeletePlacedCardContract {
    override val placedCardRepository = SqlTestCompanion.repoUnderTestContainer(
        "delete-" + random.nextInt(),
        RepoDeletePlacedCardContract.initObjects,
    )
}

@Suppress("unused")
class SqlPlacedCardRepositoryMoveTest : RepoMovePlacedCardContract {
    override val placedCardRepository = SqlTestCompanion.repoUnderTestContainer(
        "move-" + random.nextInt(),
        RepoMovePlacedCardContract.initObjects,
        randomUuid = this::uuid,
    )
}

@Suppress("unused")
class SqlPlacedCardRepositorySelectTest : RepoSelectPlacedCardContract {
    override val placedCardRepository = SqlTestCompanion.repoUnderTestContainer(
        "select-" + random.nextInt(),
        RepoSelectPlacedCardContract.initObjects,
    )
}
