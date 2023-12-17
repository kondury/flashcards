package com.github.kondury.flashcards.cards.biz.validation

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.repository.tests.StubCardRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationDeleteCardTest {

    companion object {
        private val repositoryConfig by lazy { CardRepositoryConfig(testRepository = StubCardRepository()) }
        private val corConfig by lazy { CardsCorConfig(repositoryConfig) }
        private val processor by lazy { FcCardProcessor(corConfig) }

        private const val GOOD_NOT_EMPTY_ID = "123-abs-ZX-"
        private const val GOOD_NOT_EMPTY_ID_WITH_SPACES = " \t$GOOD_NOT_EMPTY_ID \t"

        private val normalizedCard = Card(
            id = CardId(GOOD_NOT_EMPTY_ID),
            lock = FcCardLock(GOOD_NOT_EMPTY_ID)
        )
        private val denormalizedCard = Card(
            id = CardId(GOOD_NOT_EMPTY_ID_WITH_SPACES),
            lock = FcCardLock(GOOD_NOT_EMPTY_ID_WITH_SPACES),
        )
    }

    @Test
    fun `deleteCard when request is correct and normalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CardCommand.DELETE_CARD,
            configureContext = { requestCard = normalizedCard },
            assertSpecific = { context -> assertEquals(normalizedCard, context.validatedCard) }
        )

    @Test
    fun `deleteCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = CardCommand.DELETE_CARD,
            configureContext = { requestCard = denormalizedCard },
            assertSpecific = { context ->
                assertEquals(normalizedCard.id, context.validatedCard.id)
                assertEquals(normalizedCard.lock, context.validatedCard.lock)
            }
        )

    @Test
    fun `deleteCard when id is empty then validation fails`() =
        testCardIdIsNotEmptyValidation(processor, CardCommand.DELETE_CARD)

    @Test
    fun `deleteCard when id has wrong format then validation fails`() =
        testCardIdMatchesFormatValidation(processor, CardCommand.DELETE_CARD)

    @Test
    fun `deleteCard when lock is empty then validation fails`() =
        testCardLockIsNotEmptyValidation(processor, CardCommand.DELETE_CARD)

    @Test
    fun `deleteCard when lock has wrong format then validation fails`() =
        testCardLockMatchesFormatValidation(processor, CardCommand.DELETE_CARD)
}