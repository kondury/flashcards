package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardSelectDbRequest
import kotlinx.datetime.Instant
import kotlin.test.*

interface RepoSelectPlacedCardContract {
    val placedCardRepository: PlacedCardRepository

    companion object : BaseInitPlacedCards("selectPlacedCard") {
        private val userWithCards = UserId("correct-user-id")
        private val userWithoutCards = UserId("wrong-user-id")
        private val boxContainingCards = FcBox.REPEAT
        private val emptyBox = FcBox.FINISHED
        private val earliestTime = Instant.parse("2023-11-11T11:11:11Z")
        private val latestTime = Instant.parse("2023-12-12T12:12:12Z")

        private val earliestCreatedCardId = CardId("earliest-created-card-id")
        private val earliestReviewedCardId = CardId("earliest-reviewed-card-id")

        private val earliestCreatedCard = createInitTestModel(
            suffix = "earliestCreated",
            ownerId = userWithCards,
            box = boxContainingCards,
            cardId = earliestCreatedCardId,
            createdAt = earliestTime,
            updatedAt = latestTime,
        )
        private val earliestReviewedCard = createInitTestModel(
            suffix = "earliestReviewed",
            ownerId = userWithCards,
            box = boxContainingCards,
            cardId = earliestReviewedCardId,
            createdAt = latestTime,
            updatedAt = earliestTime,
        )

        override val initObjects: List<PlacedCard> = listOf(earliestCreatedCard, earliestReviewedCard)
    }

    @Test
    fun selectEarliestCreatedSuccess() = runRepoSuccessTest(
        PlacedCardSelectDbRequest(
            ownerId = userWithCards,
            strategy = FcSearchStrategy.EARLIEST_CREATED,
            box = boxContainingCards
        ),
        placedCardRepository::select,
    ) { dbResponse ->
        val data = dbResponse.data ?: fail("PlacedCardDbResponse data is not expected to be null")
        with(data) {
            assertEquals(userWithCards, ownerId)
            assertEquals(boxContainingCards, box)
            assertEquals(earliestCreatedCardId, cardId)
        }
    }

    @Test
    fun selectEarliestReviewedSuccess() = runRepoSuccessTest(
        PlacedCardSelectDbRequest(
            ownerId = userWithCards,
            strategy = FcSearchStrategy.EARLIEST_REVIEWED,
            box = boxContainingCards,
        ),
        placedCardRepository::select,
    ) { dbResponse ->
        val data = dbResponse.data ?: fail("PlacedCardDbResponse data is not expected to be null")
        with(data) {
            assertEquals(userWithCards, ownerId)
            assertEquals(boxContainingCards, box)
            assertEquals(earliestReviewedCardId, cardId)
        }
    }

    @Test
    fun selectForUserWithoutCardsReturnsNotFound() = runRepoNotFoundErrorTest(
        request = PlacedCardSelectDbRequest(
            ownerId = userWithoutCards,
            box = boxContainingCards,
            strategy = FcSearchStrategy.EARLIEST_CREATED
        ),
        placedCardRepository::select
    )

    @Test
    fun selectFromEmptyBoxReturnsNotFound() = runRepoNotFoundErrorTest(
        request = PlacedCardSelectDbRequest(
            ownerId = userWithCards,
            box = emptyBox,
            strategy = FcSearchStrategy.EARLIEST_CREATED
        ),
        placedCardRepository::select
    )
}
