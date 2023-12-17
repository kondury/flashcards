package com.github.kondury.flashcards.cards.biz.validation

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.repository.tests.StubCardRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationCreateCardTest {

    companion object {
        private val repositoryConfig by lazy { CardRepositoryConfig(testRepository = StubCardRepository()) }
        private val corConfig by lazy { CardsCorConfig(repositoryConfig) }
        private val processor by lazy { FcCardProcessor(corConfig) }

        private const val SOME_EMPTY_STRING = " \t \t"
        private const val GOOD_NOT_EMPTY_ID = "123-abs-ZX-"

        private const val GOOD_FRONT = "Front text"
        private const val GOOD_FRONT_WITH_SPACES = " $GOOD_FRONT "

        private const val GOOD_BACK = "Back text"
        private const val GOOD_BACK_WITH_SPACES = " $GOOD_BACK "


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
    }

    @Test
    fun `createCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CardCommand.CREATE_CARD,
            configureContext = { requestCard = normalizedCard },
            assertSpecific = { context -> assertEquals(normalizedCard, context.validatedCard) }
        )

    @Test
    fun `createCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CardCommand.CREATE_CARD,
            configureContext = { requestCard = denormalizedCard },
            assertSpecific = { context -> assertEquals(normalizedCard, context.validatedCard) }
        )

    @Test
    fun `createCard all three validation errors are collected`() =
        runValidationTest(
            processor = processor,
            command = CardCommand.CREATE_CARD,
            configureContext = {
                requestCard = Card(
                    id = CardId(GOOD_NOT_EMPTY_ID),
                    front = SOME_EMPTY_STRING,
                    back = SOME_EMPTY_STRING
                )
            },
            assertions = { context -> assertEquals(3, context.errors.size) }
        )

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