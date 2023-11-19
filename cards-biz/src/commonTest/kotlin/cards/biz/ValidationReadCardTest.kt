package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationReadCardTest {

    companion object {
        private val processor by lazy { FcCardProcessor() }

        private const val GOOD_NOT_EMPTY_ID = "123-abs-ZX-"
        private const val GOOD_NOT_EMPTY_ID_WITH_SPACES = " \t$GOOD_NOT_EMPTY_ID \t"

        private val normalizedCard = Card(id = CardId(GOOD_NOT_EMPTY_ID))
        private val denormalizedCard = Card(id = CardId(GOOD_NOT_EMPTY_ID_WITH_SPACES))
    }

    @Test
    fun `readCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CardCommand.READ_CARD,
            configureContext = { requestCard = normalizedCard },
            assertSpecific = { context -> assertEquals(normalizedCard, context.validatedCard) }
        )

    @Test
    fun `readCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CardCommand.READ_CARD,
            configureContext = { requestCard = denormalizedCard },
            assertSpecific = { context -> assertEquals(normalizedCard, context.validatedCard) }
        )

    @Test
    fun `readCard when id is empty then validation fails`() =
        testCardIdIsNotEmptyValidation(processor, CardCommand.READ_CARD)

    @Test
    fun `readCard when id has wrong format then validation fails`() =
        testCardIdHasProperFormatValidation(processor, CardCommand.READ_CARD)
}