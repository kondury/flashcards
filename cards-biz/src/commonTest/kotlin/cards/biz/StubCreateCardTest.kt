package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.stubs.CardStub
import kotlin.test.Test
import kotlin.test.assertEquals


class StubCreateCardTest {

    companion object {
        private val processor = FcCardProcessor()

        private val cardId = CardId("100")
        private const val FRONT = "front text"
        private const val BACK = "back text"

    }

    @Test
    fun createCardSuccess() = testSuccessStub(
        processor = processor,
        command = CardCommand.CREATE_CARD,
        configureContext = { requestCard = Card(cardId, FRONT, BACK) }
    ) { context ->
        with(context.responseCard) {
            assertEquals(CardStub.get().id, id)
            assertEquals(FRONT, front)
            assertEquals(BACK, back)
        }
    }

    @Test
    fun wrongFrontSideError() = testWrongFrontSideErrorStub(processor, CardCommand.CREATE_CARD)

    @Test
    fun wrongBackSideError() = testWrongBackSideErrorStub(processor, CardCommand.CREATE_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CardCommand.CREATE_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CardCommand.CREATE_CARD)
}