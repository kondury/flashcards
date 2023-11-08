package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.stubs.CardStub
import kotlin.test.Test
import kotlin.test.assertEquals


class CreateCardStubTest {

    companion object {
        private val processor = FcCardProcessor()

        private val id = CardId("100")
        private const val FRONT = "front text"
        private const val BACK = "back text"

        private val requestCard = Card(
            id = id,
            front = FRONT,
            back = BACK
        )
    }

    @Test
    fun createCardSuccess() = testSuccessStub(processor, CardCommand.CREATE_CARD, requestCard) { context ->
        with(context.responseCard) {
            assertEquals(CardStub.get().id, id)
            assertEquals(FRONT, front)
            assertEquals(BACK, back)
        }
    }

    @Test
    fun wrongFrontSideError() = testWrongFrontSideErrorStub(processor, CardCommand.CREATE_CARD, requestCard)

    @Test
    fun wrongBackSideError() = testWrongBackSideErrorStub(processor, CardCommand.CREATE_CARD, requestCard)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CardCommand.CREATE_CARD, requestCard)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CardCommand.CREATE_CARD, requestCard)
}