package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.Test
import kotlin.test.assertEquals


interface RepoDeleteCardContract {
    val cardRepository: CardRepository

    companion object : BaseInitCards("deleteCard") {
        override val initObjects: List<Card> = listOf(
            createInitTestModel("deleteCard"),
            createInitTestModel("deleteCardLock"),
        )
    }

    fun getExistingCard() = initObjects.first()
    fun getNonExistentCardId() = CardId("card-repo-delete-notFound")

    @Test
    fun deleteExistingWithSuccess() = runRepoTest {
        val existingCardId = getExistingCard().id
        val result = cardRepository.delete(CardIdDbRequest(existingCardId))
        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNonExistentWithSuccess() = runRepoTest {
        val nonExistentCardId = getNonExistentCardId()
        val result = cardRepository.delete(CardIdDbRequest(nonExistentCardId))
        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }
}
