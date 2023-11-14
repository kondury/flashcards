package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.CardId
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.CREATE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.UserId
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationCreatePlacedCardTest {

    companion object {

        private const val GOOD_ID = "id-1"
        private const val GOOD_ID_WITH_SPACES = " \t$GOOD_ID \t "
        private const val BAD_NOT_EMPTY_ID = "(${GOOD_ID})"

        private val expectedPlacedCard = PlacedCard(
            ownerId = UserId(GOOD_ID),
            box = FcBox.NEW,
            cardId = CardId(GOOD_ID),
        )

        val processor by lazy { FcPlacedCardProcessor() }
    }

    @Test
    fun `createPlacedCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CREATE_PLACED_CARD,
            configure = { requestPlacedCard = expectedPlacedCard }
        ) { context ->
            assertEquals(expectedPlacedCard, context.validatedPlacedCard)
        }

    @Test
    fun `createPlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CREATE_PLACED_CARD,
            configure = {
                requestPlacedCard = PlacedCard(
                    ownerId = UserId(GOOD_ID_WITH_SPACES),
                    box = FcBox.NEW,
                    cardId = CardId(GOOD_ID_WITH_SPACES),
                )
            }
        ) { context ->
            assertEquals(expectedPlacedCard, context.validatedPlacedCard)
        }

    @Test
    fun `createPlacedCard when ownerId is empty then validation fails`() =
        testOwnerIdIsNotEmptyValidation(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                box = FcBox.NEW,
                ownerId = UserId.NONE,
                cardId = CardId(GOOD_ID),
            )
        }

    @Test
    fun `createPlacedCard when ownerId has wrong format then validation fails`() =
        testOwnerIdMatchesFormatValidation(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                box = FcBox.NEW,
                ownerId = UserId(BAD_NOT_EMPTY_ID),
                cardId = CardId(GOOD_ID),
            )
        }

    @Test
    fun `createPlacedCard when cardId is empty then validation fails`() =
        testCardIdIsNotEmptyValidation(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                box = FcBox.NEW,
                ownerId = UserId(GOOD_ID),
                cardId = CardId.NONE,
            )
        }

    @Test
    fun `createPlacedCard when cardId has wrong format then validation fails`() =
        testCardIdMatchesFormatValidation(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                box = FcBox.NEW,
                ownerId = UserId(GOOD_ID),
                cardId = CardId(BAD_NOT_EMPTY_ID),
            )
        }

    @Test
    fun `createPlacedCard when box is empty then validation fails`() =
        testBoxIsNotEmptyValidation(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                box = FcBox.NONE,
                ownerId = UserId(GOOD_ID),
                cardId = CardId(GOOD_ID),
            )
        }
}