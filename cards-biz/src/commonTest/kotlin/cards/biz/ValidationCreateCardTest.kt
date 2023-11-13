package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationCreateCardTest {

    companion object {

        private val normalizedCard = Card(
            id = CardId.NONE,
            front = GOOD_FRONT,
            back = GOOD_BACK
        )

        private val denormalizedCard = Card(
            id = CardId(SOME_EMPTY_STRING),
            front = GOOD_FRONT_WITH_SPACES,
            back = GOOD_BACK_WITH_SPACES
        )

        private val requestWithThreeCreateErrors = Card(
            id = CardId(GOOD_NOT_EMPTY_ID),
            front = SOME_EMPTY_STRING,
            back = SOME_EMPTY_STRING
        )

        private val processor by lazy { FcCardProcessor() }
    }

    @Test
    fun `createCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(processor, CardCommand.CREATE_CARD, normalizedCard) { context ->
            assertEquals(normalizedCard, context.validatedCard)
        }

    @Test
    fun `createCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(processor, CardCommand.CREATE_CARD, denormalizedCard) { context ->
            assertEquals(normalizedCard, context.validatedCard)
        }

    @Test
    fun `createCard all three validation errors are collected`() =
        runValidationTest(processor, CardCommand.CREATE_CARD, requestWithThreeCreateErrors) { context ->
            assertEquals(3, context.errors.size)
        }

    @Test
    fun `createCard when id is not empty then validation fails`() =
        testCardIdIsEmptyValidation(processor, CardCommand.CREATE_CARD)

    @Test
    fun `createCard when front is empty then validation fails`() =
        testFrontIsNotEmptyValidation(processor, CardCommand.CREATE_CARD)

    @Test
    fun `createCard when back is empty then validation fails`() =
        testBackIsNotEmptyValidation(processor, CardCommand.CREATE_CARD)
}