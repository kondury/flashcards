package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.DELETE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationDeletePlacedCardTest {

    companion object {
        private val processor by lazy { FcPlacedCardProcessor() }

        private const val GOOD_ID = "id-1"
        private const val BAD_NOT_EMPTY_ID = "($GOOD_ID)"
        private const val GOOD_ID_WITH_SPACES = " \t$GOOD_ID \t "
        private val expectedPlacedCardId = PlacedCardId(GOOD_ID)
    }

    @Test
    fun `deletePlacedCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = DELETE_PLACED_CARD,
            configure = { requestPlacedCardId = expectedPlacedCardId }
        ) { context -> assertEquals(expectedPlacedCardId, context.validatedPlacedCardId) }

    @Test
    fun `deletePlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = DELETE_PLACED_CARD,
            configure = { requestPlacedCardId = PlacedCardId(GOOD_ID_WITH_SPACES) }
        ) { context -> assertEquals(expectedPlacedCardId, context.validatedPlacedCardId) }

    @Test
    fun `deletePlacedCard when placedCardId is empty then validation fails`() =
        testPlacedCardIdIsNotEmptyValidation(processor, DELETE_PLACED_CARD) {
            requestPlacedCardId = PlacedCardId.NONE
        }

    @Test
    fun `deletePlacedCard when placedCardId has wrong format then validation fails`() =
        testPlacedCardIdMatchesFormatValidation(processor, DELETE_PLACED_CARD) {
            requestPlacedCardId = PlacedCardId(BAD_NOT_EMPTY_ID)
        }

}