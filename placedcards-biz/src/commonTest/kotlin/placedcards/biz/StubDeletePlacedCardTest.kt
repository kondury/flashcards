package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.models.isEmpty
import kotlin.test.Test
import kotlin.test.assertTrue


class StubDeletePlacedCardTest {

    companion object {
        private val processor = FcPlacedCardProcessor()
        private val id = PlacedCardId("100")
    }

    @Test
    fun deletePlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = PlacedCardCommand.DELETE_PLACED_CARD,
        requestPlacedCardId = id
    ) { context ->
        assertTrue(context.responsePlacedCard.isEmpty())
    }

    @Test
    fun wrongPlacedCardIdError() = testWrongPlacedCardIdErrorStub(processor, PlacedCardCommand.DELETE_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, PlacedCardCommand.DELETE_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, PlacedCardCommand.DELETE_PLACED_CARD)
}