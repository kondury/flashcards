package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import kotlin.test.Test
import kotlin.test.assertEquals


class StubMovePlacedCardTest {

    companion object {
        private val processor = FcPlacedCardProcessor()
        private val expectedId = PlacedCardId("100")
        private val expectedBox = FcBox.REPEAT
    }

    @Test
    fun movePlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = PlacedCardCommand.MOVE_PLACED_CARD,
        configureContext = {
            requestPlacedCard = PlacedCard(
                id = expectedId,
                box = expectedBox
            )
        },
    ) { context ->
        with(context.responsePlacedCard) {
            assertEquals(expectedId, id)
            assertEquals(expectedBox, box)
        }
    }

    @Test
    fun wrongPlacedCardIdError() = testWrongPlacedCardIdErrorStub(processor, PlacedCardCommand.MOVE_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, PlacedCardCommand.MOVE_PLACED_CARD)

    @Test
    fun wrongNotFoundError() = testNotFoundStubError(processor, PlacedCardCommand.MOVE_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, PlacedCardCommand.MOVE_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, PlacedCardCommand.MOVE_PLACED_CARD)
}