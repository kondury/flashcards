package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidationDeleteCardTest {

    companion object {
        private val processor by lazy { FcCardProcessor() }

        private val normalizedCard = Card(id = CardId(GOOD_NOT_EMPTY_ID))
        private val denormalizedCard = Card(id = CardId(GOOD_NOT_EMPTY_ID_WITH_SPACES))
    }

    @Test
    fun `deleteCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(processor, CardCommand.DELETE_CARD, normalizedCard) { context ->
            assertEquals(normalizedCard, context.validatedCard)
        }

    @Test
    fun `deleteCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(processor, CardCommand.DELETE_CARD, denormalizedCard) { context ->
            assertEquals(normalizedCard, context.validatedCard)
        }

    @Test
    fun `deleteCard when id is empty then validation fails`() =
        testCardIdIsNotEmptyValidation(processor, CardCommand.DELETE_CARD)

    @Test
    fun `deleteCard when id has wrong format then validation fails`() =
        testCardIdHasProperFormatValidation(processor, CardCommand.DELETE_CARD)
}