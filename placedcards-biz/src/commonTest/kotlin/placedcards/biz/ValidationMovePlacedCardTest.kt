package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.MOVE_PLACED_CARD
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationMovePlacedCardTest {

    companion object {
        private val processor by lazy { FcPlacedCardProcessor() }

        private const val GOOD_ID = "id-1"
        private const val BAD_NOT_EMPTY_ID = "($GOOD_ID)"
        private const val GOOD_ID_WITH_SPACES = " \t$GOOD_ID \t "
        private val expectedPlacedCardId = PlacedCardId(GOOD_ID)
        private val expectedBoxAfter = FcBox.REPEAT
    }

    @Test
    fun `movePlacedCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = MOVE_PLACED_CARD,
            configure = {
                requestPlacedCardId = expectedPlacedCardId
                requestBoxAfter = expectedBoxAfter
            }
        ) { context ->
            assertEquals(expectedPlacedCardId, context.validatedPlacedCardId)
            assertEquals(expectedBoxAfter, context.validatedBoxAfter)
        }

    @Test
    fun `movePlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = MOVE_PLACED_CARD,
            configure = {
                requestPlacedCardId = PlacedCardId(GOOD_ID_WITH_SPACES)
                requestBoxAfter = expectedBoxAfter
            }
        ) { context ->
            assertEquals(expectedPlacedCardId, context.validatedPlacedCardId)
            assertEquals(expectedBoxAfter, context.validatedBoxAfter)
        }

    @Test
    fun `movePlacedCard when placedCardId is empty then validation fails`() =
        testPlacedCardIdIsNotEmptyValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCardId = PlacedCardId.NONE
            requestBoxAfter = expectedBoxAfter
        }

    @Test
    fun `movePlacedCard when placedCardId has wrong format then validation fails`() =
        testPlacedCardIdMatchesFormatValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCardId = PlacedCardId(BAD_NOT_EMPTY_ID)
            requestBoxAfter = expectedBoxAfter
        }

    @Test
    fun `movePlacedCard when boxAfter is empty then validation fails`() =
        testBoxIsNotEmptyValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCardId = PlacedCardId(GOOD_ID)
            requestBoxAfter = FcBox.NONE
        }

}