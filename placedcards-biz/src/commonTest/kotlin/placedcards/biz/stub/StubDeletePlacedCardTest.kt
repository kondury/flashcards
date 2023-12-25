package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.models.isEmpty
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import kotlin.test.Test
import kotlin.test.assertTrue


class StubDeletePlacedCardTest {

    companion object {
        private val processor = initProcessor(PlacedCardRepository.NoOpPlacedCardRepository)
        private val placedCardId = PlacedCardId("100")
    }

    @Test
    fun deletePlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = PlacedCardCommand.DELETE_PLACED_CARD,
        configureContext = {
            requestPlacedCard = PlacedCard(id = placedCardId)
        }
    ) { context -> assertTrue(context.responsePlacedCard.isEmpty()) }

    @Test
    fun wrongPlacedCardIdError() = testWrongPlacedCardIdErrorStub(processor, PlacedCardCommand.DELETE_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, PlacedCardCommand.DELETE_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, PlacedCardCommand.DELETE_PLACED_CARD)
}