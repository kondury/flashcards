package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.SELECT_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.UserId
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationSelectPlacedCardTest {

    companion object {

        private const val GOOD_ID = "id-1"
        private const val GOOD_ID_WITH_SPACES = " \t$GOOD_ID \t "
        private const val BAD_NOT_EMPTY_ID = "(${GOOD_ID})"

        private val expectedSearchStrategy = FcSearchStrategy.EARLIEST_REVIEWED
        private val expectedOwnerId = UserId(GOOD_ID)
        private val expectedBox = FcBox.NEW

        private val processor by lazy { FcPlacedCardProcessor() }
    }

    @Test
    fun `selectPlacedCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = SELECT_PLACED_CARD,
            configure = {
                requestWorkBox = expectedBox
                requestSearchStrategy = expectedSearchStrategy
                requestOwnerId = expectedOwnerId
            }
        ) { context ->
            assertEquals(expectedBox, context.validatedWorkBox)
            assertEquals(expectedSearchStrategy, context.validatedSearchStrategy)
            assertEquals(expectedOwnerId, context.validatedOwnerId)
        }

    @Test
    fun `selectPlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = SELECT_PLACED_CARD,
            configure = {
                requestWorkBox = expectedBox
                requestSearchStrategy = expectedSearchStrategy
                requestOwnerId = UserId(GOOD_ID_WITH_SPACES)
            }
        ) { context ->
            assertEquals(expectedBox, context.validatedWorkBox)
            assertEquals(expectedSearchStrategy, context.validatedSearchStrategy)
            assertEquals(expectedOwnerId, context.validatedOwnerId)
        }

    @Test
    fun `selectPlacedCard when ownerId is empty then validation fails`() =
        testOwnerIdIsNotEmptyValidation(processor, SELECT_PLACED_CARD) {
            requestWorkBox = FcBox.NEW
            requestSearchStrategy = FcSearchStrategy.EARLIEST_REVIEWED
            requestOwnerId = UserId.NONE
        }

    @Test
    fun `selectPlacedCard when ownerId has wrong format then validation fails`() =
        testOwnerIdMatchesFormatValidation(processor, SELECT_PLACED_CARD) {
            requestWorkBox = FcBox.NEW
            requestSearchStrategy = FcSearchStrategy.EARLIEST_REVIEWED
            requestOwnerId = UserId(BAD_NOT_EMPTY_ID)
        }

    @Test
    fun `selectPlacedCard when searchStrategy is empty then validation fails`() =
        testSearchStrategyIsNotEmptyValidation(processor, SELECT_PLACED_CARD) {
            requestWorkBox = FcBox.NEW
            requestSearchStrategy = FcSearchStrategy.NONE
            requestOwnerId = UserId(GOOD_ID)
        }

    @Test
    fun `selectPlacedCard when workBox is empty then validation fails`() =
        testBoxIsNotEmptyValidation(processor, SELECT_PLACED_CARD) {
            requestWorkBox = FcBox.NONE
            requestSearchStrategy = FcSearchStrategy.EARLIEST_CREATED
            requestOwnerId = UserId(GOOD_ID)
        }
}