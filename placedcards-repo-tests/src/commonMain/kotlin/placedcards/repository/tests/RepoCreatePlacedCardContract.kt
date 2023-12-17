package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.NONE
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbRequest
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.fail

interface RepoCreatePlacedCardContract {
    val placedCardRepository: PlacedCardRepository
    val uuid: String
        get() = goodLock.asString()

    companion object : BaseInitPlacedCards("createPlacedCard") {
        private val expectedBox = FcBox.NEW
        private val expectedUserId = UserId("user-1")
        private val expectedCardId = CardId("card-1")
        private val goodLock = FcPlacedCardLock("20000000-0000-0000-0000-000000000001")
        private val requestCard = PlacedCard(
            ownerId = expectedUserId,
            box = expectedBox,
            cardId = expectedCardId,
        )
        override val initObjects: List<PlacedCard> = emptyList()
    }

    @Test
    fun createPlacedCardSuccess() = runRepoSuccessTest(
        PlacedCardDbRequest(requestCard),
        placedCardRepository::create
    ) { dbResponse ->
        val data = dbResponse.data ?: fail("PlacedCardDbResponse data is not expected to be null")
        with(data) {
            assertEquals(expectedUserId, ownerId)
            assertEquals(expectedBox, box)
            assertEquals(expectedCardId, cardId)
            assertNotEquals(PlacedCardId.NONE, id)
            assertNotEquals(Instant.NONE, createdAt)
            assertNotEquals(Instant.NONE, updatedAt)
            assertEquals(goodLock, lock)
        }
    }
}
