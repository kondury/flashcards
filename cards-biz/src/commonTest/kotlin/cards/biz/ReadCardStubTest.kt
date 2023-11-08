package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.stubs.CardStub
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadCardStubTest {
    companion object {
        private val processor = FcCardProcessor()
        private val id = CardId("100")
        private val requestCard = Card(id = id)
    }

    @Test
    fun readCardSuccess() = testSuccessStub(processor, CardCommand.READ_CARD, requestCard) { context ->
        assertEquals(CardStub.getWith(id = id), context.responseCard)
    }

    @Test
    fun wrongCardIdError() = testWrongCardIdErrorStub(processor, CardCommand.READ_CARD, requestCard)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CardCommand.READ_CARD, requestCard)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CardCommand.READ_CARD, requestCard)

    @Test fun stubNotFoundError() = testNotFoundStubError(processor, CardCommand.READ_CARD, requestCard)
}