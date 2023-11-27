package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


interface RepoCreateCardContract {
    val cardRepository: CardRepository

    companion object : BaseInitCards("createCard") {
        override val initObjects: List<Card> = emptyList()
    }

    fun getRequestCard() = Card(
        front = "createCard front",
        back = "createCard back",
    )

    @Test
    fun createCardSuccess() = runRepoTest {
        val result = cardRepository.create(CardDbRequest(getRequestCard()))
        val expected = getRequestCard().copy(id = result.data?.id ?: CardId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.front, result.data?.front)
        assertEquals(expected.back, result.data?.back)
        assertNotEquals(CardId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }
}
