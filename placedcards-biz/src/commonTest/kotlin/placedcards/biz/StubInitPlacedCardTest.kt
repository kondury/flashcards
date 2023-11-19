package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.INIT_PLACED_CARD
import kotlin.test.Test
import kotlin.test.assertTrue


class StubInitPlacedCardTest {

    companion object {
        private val processor = FcPlacedCardProcessor()
        private val expectedOwnerId = UserId("user-id")
        private val expectedBox = FcBox.NEW
    }

    @Test
    fun initPlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = INIT_PLACED_CARD,
        configureContext = {
            requestOwnerId = expectedOwnerId
            requestWorkBox = expectedBox
        }
    ) { context ->
        assertTrue(context.responsePlacedCard.isEmpty())
    }

    @Test
    fun wrongOwnerId() = testWrongOwnerIdErrorStub(processor, INIT_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, INIT_PLACED_CARD)

    @Test
    fun wrongNotFoundError() = testNotFoundStubError(processor, INIT_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, INIT_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, INIT_PLACED_CARD)
}