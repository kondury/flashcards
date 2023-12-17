package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.*


interface RepoDeleteCardContract {
    val cardRepository: CardRepository

    companion object : BaseInitCards("deleteCard") {
        private val expectedLock = FcCardLock("20000000-0000-0000-0000-000000000001")
        private val nonExpectedLock = FcCardLock("20000000-0000-0000-0000-000000000009")
        private val existingCard = createInitTestModel("deleteCard", expectedLock)
        private val badLockTestCard = createInitTestModel("deleteCardLock", expectedLock)
        private val nonExistentCardId = CardId("card-repo-deleteCard-notFound")

        override val initObjects: List<Card> = listOf(existingCard, badLockTestCard)
    }

    @Test
    fun deleteExistingWithSuccess() = runRepoTest {
        val request = CardIdDbRequest(existingCard.id, lock = expectedLock)
        val dbResponse = cardRepository.delete(request)
        assertEquals(true, dbResponse.isSuccess)
        assertTrue(dbResponse.errors.isEmpty(), "Errors list is expected to be empty")
        assertEquals(null, dbResponse.data)
    }

    @Test
    fun deleteCardNonExistentWithError() = runRepoTest {
        val request = CardIdDbRequest(nonExistentCardId, lock = expectedLock)
        val dbResponse = cardRepository.delete(request)
        assertEquals(false, dbResponse.isSuccess)
        assertEquals(null, dbResponse.data)
        val error = dbResponse.errors.find { it.code == "not-found" }
            ?: fail("Errors list is expected to contain not-found error")
        assertEquals("id", error.field)
    }

    @Test
    fun deleteCardConcurrencyError() = runRepoTest {
        val request = CardIdDbRequest(badLockTestCard.id, lock = nonExpectedLock)
        val dbResponse = cardRepository.delete(request)
        assertEquals(false, dbResponse.isSuccess)
        assertNotNull(dbResponse.data, "Data is expected to contain a card instance from repository having the 'bad' lock")
        val error = dbResponse.errors.find { it.code == "concurrency" }
            ?: fail("Errors list is expected to contain concurrency error")
        assertEquals("lock", error.field)
    }
}
