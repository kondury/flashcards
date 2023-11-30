package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


import kotlin.test.fail

interface RepoCreateCardContract {
    val cardRepository: CardRepository
    val uuid: String
        get() = goodLock.asString()

    companion object : BaseInitCards("createCard") {
        private const val FRONT_TEXT = "createCard front"
        private const val BACK_TEXT = "createCard back"
        val goodLock = FcCardLock("20000000-0000-0000-0000-000000000001")
        private val requestCard = Card(
            front = FRONT_TEXT,
            back = BACK_TEXT,
        )
        override val initObjects: List<Card> = emptyList()
    }

    @Test
    fun createCardSuccess() = runRepoTest {
        val dbResponse = cardRepository.create(CardDbRequest(requestCard))
        assertEquals(true, dbResponse.isSuccess)
        assertEquals(emptyList(), dbResponse.errors)
        val data = dbResponse.data ?: fail("CardDbResponse data is not expected to be null")
        with (data) {
            assertEquals(FRONT_TEXT, front)
            assertEquals(BACK_TEXT, back)
            assertNotEquals(CardId.NONE, id)
            assertEquals(goodLock, lock)
        }
    }
}
