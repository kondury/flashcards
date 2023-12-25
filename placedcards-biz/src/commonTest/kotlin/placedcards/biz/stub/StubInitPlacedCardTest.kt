package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.common.models.isEmpty
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import kotlin.test.Test
import kotlin.test.assertTrue


class StubInitPlacedCardTest {

    companion object {
        private val processor = initProcessor(PlacedCardRepository.NoOpPlacedCardRepository)
        private val expectedOwnerId = UserId("user-id")
        private val expectedBox = FcBox.NEW
    }

    @Test
    fun initPlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = PlacedCardCommand.INIT_PLACED_CARD,
        configureContext = {
            requestOwnerId = expectedOwnerId
            requestWorkBox = expectedBox
        }
    ) { context ->
        assertTrue(context.responsePlacedCard.isEmpty())
    }

    @Test
    fun wrongOwnerId() = testWrongOwnerIdErrorStub(processor, PlacedCardCommand.INIT_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, PlacedCardCommand.INIT_PLACED_CARD)

    @Test
    fun wrongNotFoundError() = testNotFoundStubError(processor, PlacedCardCommand.INIT_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, PlacedCardCommand.INIT_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, PlacedCardCommand.INIT_PLACED_CARD)
}