package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.Test
import kotlin.test.assertEquals


import kotlin.test.fail

interface RepoReadCardContract {
    val cardRepository: CardRepository

    companion object : BaseInitCards("readCard") {
        private val existingCard = createInitTestModel("readCard")
        private val nonExistentCardId = CardId("card-repo-readCard-notFound")

        override val initObjects: List<Card> = listOf(existingCard)
    }

    @Test
    fun readCardSuccess() = runRepoTest {
        val request = CardIdDbRequest(existingCard.id)
        val dbResponse = cardRepository.read(request)

        assertEquals(true, dbResponse.isSuccess)
        assertEquals(existingCard, dbResponse.data)
        assertEquals(emptyList(), dbResponse.errors)
    }

    @Test
    fun readCardNotFound() = runRepoTest {
        val request = CardIdDbRequest(nonExistentCardId)
        val dbResponse = cardRepository.read(request)
        assertEquals(false, dbResponse.isSuccess)
        assertEquals(null, dbResponse.data)
        val error = dbResponse.errors.find { it.code == "not-found" }
            ?: fail("Errors list is expected to contain not-found error")
        assertEquals("id", error.field)
    }
}
