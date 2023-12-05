package com.github.kondury.flashcards.cards.repository.sql

import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.tests.RepoCreateCardContract
import com.github.kondury.flashcards.cards.repository.tests.RepoDeleteCardContract
import com.github.kondury.flashcards.cards.repository.tests.RepoReadCardContract
import kotlin.random.Random

val random = Random(System.currentTimeMillis())

@Suppress("unused")
class SqlCardRepositoryCreateTest : RepoCreateCardContract {
    override val cardRepository: CardRepository = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        RepoCreateCardContract.initObjects,
        randomUuid = this::uuid,
    )
}

@Suppress("unused")
class SqlCardRepositoryDeleteTest : RepoDeleteCardContract {
    override val cardRepository: CardRepository = SqlTestCompanion.repoUnderTestContainer(
        "delete_" + random.nextInt(),
        RepoDeleteCardContract.initObjects
    )
}

@Suppress("unused")
class SqlCardRepositoryReadTest : RepoReadCardContract {
    override val cardRepository: CardRepository = SqlTestCompanion.repoUnderTestContainer(
        "read" + random.nextInt(),
        RepoReadCardContract.initObjects
    )
}
