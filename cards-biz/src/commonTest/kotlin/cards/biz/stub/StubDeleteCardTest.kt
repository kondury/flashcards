package com.github.kondury.flashcards.cards.biz.stub

import com.github.kondury.flashcards.cards.biz.common.initProcessor
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.isEmpty
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import kotlin.test.Test
import kotlin.test.assertTrue


class StubDeleteCardTest {

    companion object {
        private val processor = initProcessor(CardRepository.NoOpCardRepository)

        private val cardId = CardId("100")
    }

    @Test
    fun deleteCardSuccess() = testSuccessStub(
        processor = processor,
        command = CardCommand.DELETE_CARD,
        configureContext = { requestCard = Card(id = cardId) },
        assertSuccessSpecific = { context -> assertTrue(context.responseCard.isEmpty()) }
    )

    @Test
    fun wrongCardIdError() = testWrongCardIdErrorStub(processor, CardCommand.DELETE_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CardCommand.DELETE_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CardCommand.DELETE_CARD)
}