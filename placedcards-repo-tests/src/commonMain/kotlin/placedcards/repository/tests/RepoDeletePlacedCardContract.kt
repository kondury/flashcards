package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardIdDbRequest
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

interface RepoDeletePlacedCardContract {
    val placedCardRepository: PlacedCardRepository

    companion object : BaseInitPlacedCards("deletePlacedCard") {
        private val initCreatedAt = Clock.System.now().let {
            val sec = it.epochSeconds
            val nano = it.nanosecondsOfSecond / 1000 * 1000
            Instant.fromEpochSeconds(sec, nano)
        }
        private val expectedLock = FcPlacedCardLock("20000000-0000-0000-0000-000000000001")
        private val nonExpectedLock = FcPlacedCardLock("20000000-0000-0000-0000-000000000009")
        private val existingPlacedCard = createInitTestModel(
            suffix = "delete",
            lock = expectedLock,
            createdAt = initCreatedAt,
            updatedAt = initCreatedAt,
        )
        private val badLockTestPlacedCard = createInitTestModel(
            suffix = "deleteLock",
            lock = expectedLock,
            createdAt = initCreatedAt,
            updatedAt = initCreatedAt,
        )
        private val nonExistentPlacedCardId = PlacedCardId("card-repo-deletePlacedCard-notFound")

        override val initObjects: List<PlacedCard> = listOf(existingPlacedCard, badLockTestPlacedCard)
    }

    @Test
    fun deletePlacedCardExistingWithSuccess() = runRepoSuccessTest(
        PlacedCardIdDbRequest(
            id = existingPlacedCard.id,
            lock = existingPlacedCard.lock,
        ),
        placedCardRepository::delete,
    ) { dbResponse ->
        assertEquals(null, dbResponse.data)
    }

    @Test
    fun deletePlacedCardNonExistentWithError() = runRepoNotFoundErrorTest(
        PlacedCardIdDbRequest(
            id = nonExistentPlacedCardId,
            lock = expectedLock
        ),
        placedCardRepository::delete,
    )

    @Test
    fun deletePlacedCardConcurrencyError() = runRepoConcurrencyErrorTest(
        PlacedCardIdDbRequest(
            id = badLockTestPlacedCard.id,
            lock = nonExpectedLock
        ),
        placedCardRepository::delete
    )

}
