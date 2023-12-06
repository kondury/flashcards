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
                requestPlacedCard = PlacedCard(
                    id = expectedPlacedCardId,
                    box = expectedBoxAfter
                )
            }
        ) { context ->
            assertEquals(expectedPlacedCardId, context.validatedPlacedCard.id)
            assertEquals(expectedBoxAfter, context.validatedPlacedCard.box)
        }

    @Test
    fun `movePlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = MOVE_PLACED_CARD,
            configure = {
                requestPlacedCard = PlacedCard(
                    id = PlacedCardId(GOOD_ID_WITH_SPACES),
                    box = expectedBoxAfter
                )
            }
        ) { context ->
            assertEquals(expectedPlacedCardId, context.validatedPlacedCard.id)
            assertEquals(expectedBoxAfter, context.validatedPlacedCard.box)
        }

    @Test
    fun `movePlacedCard when placedCardId is empty then validation fails`() =
        testPlacedCardIdIsNotEmptyValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId.NONE,
                box = expectedBoxAfter
            )
        }

    @Test
    fun `movePlacedCard when placedCardId has wrong format then validation fails`() =
        testPlacedCardIdMatchesFormatValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(BAD_NOT_EMPTY_ID),
                box = expectedBoxAfter
            )
        }

    @Test
    fun `movePlacedCard when boxAfter is empty then validation fails`() =
        testBoxIsNotEmptyValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(GOOD_ID),
                box = FcBox.NONE
            )
        }

}