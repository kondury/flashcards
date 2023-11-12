package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.isEmpty
import kotlin.test.Test
import kotlin.test.assertTrue


class StubDeleteCardTest {

    companion object {
        private val processor = FcCardProcessor()
        private val id = CardId("100")
        private val requestCard = Card(id = id)
    }

    @Test
    fun deleteCardSuccess() = testSuccessStub(processor, CardCommand.DELETE_CARD, requestCard) { context ->
        assertTrue(context.responseCard.isEmpty())
    }

    @Test
    fun wrongCardIdError() = testWrongCardIdErrorStub(processor, CardCommand.DELETE_CARD, requestCard)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CardCommand.DELETE_CARD, requestCard)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CardCommand.DELETE_CARD, requestCard)
}