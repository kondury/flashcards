package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.INIT_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.repository.tests.StubPlacedCardRepository
import kotlin.test.Test
import kotlin.test.assertEquals


class ValidationInitPlacedCardTest {

    companion object {
        private val processor by lazy { initProcessor(StubPlacedCardRepository()) }

        private const val GOOD_ID = "id-1"
        private const val GOOD_ID_WITH_SPACES = " \t$GOOD_ID \t "
        private val expectedOwnerId = UserId(GOOD_ID)
        private val expectedBox = FcBox.NEW
    }

    @Test
    fun `initPlacedCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = INIT_PLACED_CARD,
            configureContext = {
                requestWorkBox = expectedBox
                requestOwnerId = expectedOwnerId
            }
        ) { context ->
            assertEquals(expectedBox, context.validatedWorkBox)
            assertEquals(expectedOwnerId, context.validatedOwnerId)
        }

    @Test
    fun `initPlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = INIT_PLACED_CARD,
            configureContext = {
                requestWorkBox = expectedBox
                requestOwnerId = UserId(GOOD_ID_WITH_SPACES)
            }
        ) { context ->
            assertEquals(expectedBox, context.validatedWorkBox)
            assertEquals(expectedOwnerId, context.validatedOwnerId)
        }

    @Test
    fun `initPlacedCard when ownerId is empty then validation fails`() =
        testOwnerIdIsNotEmptyValidation(processor, INIT_PLACED_CARD) {
            requestWorkBox = FcBox.NEW
            requestOwnerId = UserId.NONE
        }

    @Test
    fun `initPlacedCard when workBox is empty then validation fails`() =
        testBoxIsNotEmptyValidation(processor, INIT_PLACED_CARD) {
            requestWorkBox = FcBox.NONE
            requestOwnerId = UserId(GOOD_ID)
        }
}