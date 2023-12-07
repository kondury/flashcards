package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardMoveDbRequest
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

interface RepoMovePlacedCardContract {
    val placedCardRepository: PlacedCardRepository
    val uuid: String
        get() = lockAfter.asString()

    companion object : BaseInitPlacedCards("movePlacedCard") {
        private val lockBefore = FcPlacedCardLock("20000000-0000-0000-0000-000000000001")
        private val lockAfter = FcPlacedCardLock("20000000-0000-0000-0000-000000000002")

        private val initCreatedAt = Clock.System.now().let {
            val sec = it.epochSeconds
            val nano = it.nanosecondsOfSecond / 1000 * 1000
            Instant.fromEpochSeconds(sec, nano)
        }

        private val existingPlacedCard = createInitTestModel(
            suffix = "move",
            lock = lockBefore,
            box = FcBox.NEW,
            createdAt = initCreatedAt,
            updatedAt = initCreatedAt
        )

        private val badLockTestPlacedCard = createInitTestModel(
            suffix = "moveLock",
            lock = lockBefore,
            box = FcBox.NEW,
            createdAt = initCreatedAt,
            updatedAt = initCreatedAt
        )

        override val initObjects: List<PlacedCard> = listOf(existingPlacedCard, badLockTestPlacedCard)

        private val nonExpectedLock = FcPlacedCardLock("20000000-0000-0000-0000-000000000009")
        private val nonExistentPlacedCardId = PlacedCardId("card-repo-movePlacedCard-notFound")
    }

    @Test
    fun movePlacedCardExistingWithSuccess() = runRepoSuccessTest(
        PlacedCardMoveDbRequest(
            id = existingPlacedCard.id,
            lock = existingPlacedCard.lock,
            box = FcBox.REPEAT
        ),
        placedCardRepository::move,
    ) { dbResponse ->
        val data = dbResponse.data ?: fail("PlacedCardDbResponse data is not expected to be null")
        with(data) {
            assertEquals(existingPlacedCard.id, id)
            assertEquals(lockAfter, lock)
            assertEquals(existingPlacedCard.ownerId, ownerId)
            assertEquals(existingPlacedCard.cardId, cardId)
            assertEquals(initCreatedAt, createdAt)
            assertTrue(initCreatedAt <= updatedAt)
            assertEquals(FcBox.REPEAT, box)
        }
    }

    @Test
    fun movePlacedCardNonExistentWithNotFoundError() = runRepoNotFoundErrorTest(
        PlacedCardMoveDbRequest(
            id = nonExistentPlacedCardId,
            lock = lockBefore,
            box = FcBox.REPEAT
        ),
        placedCardRepository::move
    )

    @Test
    fun movePlacedCardConcurrencyError() = runRepoConcurrencyErrorTest(
        PlacedCardMoveDbRequest(
            id = badLockTestPlacedCard.id,
            lock = nonExpectedLock,
            box = FcBox.REPEAT
        ),
        placedCardRepository::move
    )
}

