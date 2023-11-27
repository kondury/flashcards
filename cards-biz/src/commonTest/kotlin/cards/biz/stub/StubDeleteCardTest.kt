package com.github.kondury.flashcards.cards.biz.stub

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.isEmpty
import kotlin.test.Test
import kotlin.test.assertTrue


class StubDeleteCardTest {

    companion object {
        private val cardRepositoryConfig = CardRepositoryConfig.NONE
        private val cardsCorConfig = CardsCorConfig(cardRepositoryConfig)
        private val processor = FcCardProcessor(cardsCorConfig)


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