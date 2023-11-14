package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.stubs.CardStub
import kotlin.test.Test
import kotlin.test.assertEquals

class StubReadCardTest {
    companion object {
        private val processor = FcCardProcessor()
        private val cardId = CardId("100")
    }

    @Test
    fun readCardSuccess() = testSuccessStub(
        processor = processor,
        command = CardCommand.READ_CARD,
        configureContext = { requestCard = Card(id = cardId) },
        assertSuccessSpecific = { context -> assertEquals(CardStub.getWith(id = cardId), context.responseCard) }
    )

    @Test
    fun wrongCardIdError() = testWrongCardIdErrorStub(processor, CardCommand.READ_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CardCommand.READ_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CardCommand.READ_CARD)

    @Test
    fun stubNotFoundError() = testNotFoundStubError(processor, CardCommand.READ_CARD)
}