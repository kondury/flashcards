package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.SELECT_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.UserId
import kotlin.test.Test
import kotlin.test.assertEquals


class StubSelectPlacedCardTest {

    companion object {
        private val processor = FcPlacedCardProcessor()
        private val expectedOwnerId = UserId("user-id")
        private val expectedBox = FcBox.FINISHED
        private val expectedStrategy = FcSearchStrategy.EARLIEST_CREATED
    }

    @Test
    fun selectPlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = SELECT_PLACED_CARD,
        configureContext = {
            requestOwnerId = expectedOwnerId
            requestWorkBox = expectedBox
            requestSearchStrategy = expectedStrategy
        }
    ) { context ->
        with(context.responsePlacedCard) {
            assertEquals(expectedOwnerId, ownerId)
            assertEquals(expectedBox, box)
        }
    }

    @Test
    fun wrongOwnerId() = testWrongOwnerIdErrorStub(processor, SELECT_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, SELECT_PLACED_CARD)

    @Test
    fun wrongSearchStrategy() = testWrongSearchStrategyErrorStub(processor, SELECT_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, SELECT_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, SELECT_PLACED_CARD)
}