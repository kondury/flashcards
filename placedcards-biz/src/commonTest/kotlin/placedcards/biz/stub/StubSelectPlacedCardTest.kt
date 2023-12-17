package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.models.UserId
import kotlin.test.Test
import kotlin.test.assertEquals

class StubSelectPlacedCardTest {

    companion object {
        private val placedCardsRepositoryConfig = PlacedCardsCorConfig.NONE
        private val placedCardsCorConfig = PlacedCardsCorConfig(placedCardsRepositoryConfig)
        private val processor = FcPlacedCardProcessor(placedCardsCorConfig)

        private val expectedOwnerId = UserId("user-id")
        private val expectedBox = FcBox.FINISHED
        private val expectedStrategy = FcSearchStrategy.EARLIEST_CREATED
    }

    @Test
    fun selectPlacedCardSuccess() = runSuccessStubTest(
        processor = processor,
        command = PlacedCardCommand.SELECT_PLACED_CARD,
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
    fun wrongOwnerId() = testWrongOwnerIdErrorStub(processor, PlacedCardCommand.SELECT_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, PlacedCardCommand.SELECT_PLACED_CARD)

    @Test
    fun wrongSearchStrategy() = testWrongSearchStrategyErrorStub(processor, PlacedCardCommand.SELECT_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, PlacedCardCommand.SELECT_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, PlacedCardCommand.SELECT_PLACED_CARD)
}