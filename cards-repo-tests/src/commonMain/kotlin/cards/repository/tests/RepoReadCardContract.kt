package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.Test
import kotlin.test.assertEquals


interface RepoReadCardContract {
    val cardRepository: CardRepository

    companion object : BaseInitCards("readCard") {
        override val initObjects: List<Card> = listOf(
            createInitTestModel("readCard")
        )
    }

    fun getExistingCard() = initObjects.first()
    fun getNonExistentCardId() = CardId("card-repo-readCard-notFound")

    @Test
    fun readCardSuccess() = runRepoTest {
        val existingCard = getExistingCard()
        val result = cardRepository.read(CardIdDbRequest(existingCard.id))

        assertEquals(true, result.isSuccess)
        assertEquals(existingCard, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readCardNotFound() = runRepoTest {
        val nonExistentCardId = getNonExistentCardId()
        val result = cardRepository.read(CardIdDbRequest(nonExistentCardId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }
}
