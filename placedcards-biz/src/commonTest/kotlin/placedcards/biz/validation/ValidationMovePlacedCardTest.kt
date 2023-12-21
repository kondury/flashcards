package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.MOVE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.repository.tests.StubPlacedCardRepository
import kotlin.test.Test
import kotlin.test.assertEquals


class ValidationMovePlacedCardTest {

    companion object {
        private val processor by lazy { initProcessor(StubPlacedCardRepository()) }

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
            configureContext = {
                requestPlacedCard = PlacedCard(
                    id = expectedPlacedCardId,
                    lock = FcPlacedCardLock(GOOD_ID),
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
            configureContext = {
                requestPlacedCard = PlacedCard(
                    id = PlacedCardId(GOOD_ID_WITH_SPACES),
                    lock = FcPlacedCardLock(GOOD_ID),
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
                lock = FcPlacedCardLock(GOOD_ID),
                box = expectedBoxAfter
            )
        }

    @Test
    fun `movePlacedCard when placedCardId has wrong format then validation fails`() =
        testPlacedCardIdMatchesFormatValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(BAD_NOT_EMPTY_ID),
                lock = FcPlacedCardLock(GOOD_ID),
                box = expectedBoxAfter
            )
        }

    @Test
    fun `movePlacedCard when boxAfter is empty then validation fails`() =
        testBoxIsNotEmptyValidation(processor, MOVE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(GOOD_ID),
                lock = FcPlacedCardLock(GOOD_ID),
                box = FcBox.NONE
            )
        }

    @Test
    fun `movePlacedCard when lock is empty then validation fails`() =
        testPlacedCardLockIsNotEmptyValidation(
            processor,
            MOVE_PLACED_CARD
        ) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(GOOD_ID),
                lock = FcPlacedCardLock.NONE,
                box = expectedBoxAfter
            )
        }

    @Test
    fun `movePlacedCard when lock has wrong format then validation fails`() =
        testPlacedCardLockMatchesFormatValidation(
            processor,
            MOVE_PLACED_CARD
        ) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(GOOD_ID),
                lock = FcPlacedCardLock(BAD_NOT_EMPTY_ID),
                box = expectedBoxAfter                
            )
        }

}