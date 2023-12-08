package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.DELETE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.repository.tests.StubPlacedCardRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationDeletePlacedCardTest {

    companion object {
        private val repositoryConfig by lazy { PlacedCardRepositoryConfig(testRepository = StubPlacedCardRepository()) }
        private val corConfig by lazy { PlacedCardsCorConfig(repositoryConfig) }
        private val processor by lazy { FcPlacedCardProcessor(corConfig) }

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
            configureContext = {
                requestPlacedCard = PlacedCard(
                    id = expectedPlacedCardId,
                    lock = FcPlacedCardLock(GOOD_ID)
                )
            }
        ) { context ->
            assertEquals(expectedPlacedCardId, context.validatedPlacedCard.id)
        }

    @Test
    fun `deletePlacedCard when request is correct and denormalized then validation is successful`() =
        runSuccessfulValidationTest(
            processor = processor,
            command = DELETE_PLACED_CARD,
            configureContext = {
                requestPlacedCard = PlacedCard(
                    id = PlacedCardId(GOOD_ID_WITH_SPACES),
                    lock = FcPlacedCardLock(GOOD_ID)
                )
            }
        ) { context ->
            assertEquals(expectedPlacedCardId, context.validatedPlacedCard.id)
        }

    @Test
    fun `deletePlacedCard when placedCardId is empty then validation fails`() =
        testPlacedCardIdIsNotEmptyValidation(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId.NONE,
                lock = FcPlacedCardLock(GOOD_ID)
            )
        }

    @Test
    fun `deletePlacedCard when placedCardId has wrong format then validation fails`() =
        testPlacedCardIdMatchesFormatValidation(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(BAD_NOT_EMPTY_ID),
                lock = FcPlacedCardLock(GOOD_ID)
            )
        }

    @Test
    fun `deletePlacedCard when lock is empty then validation fails`() =
        testPlacedCardLockIsNotEmptyValidation(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(GOOD_ID),
                lock = FcPlacedCardLock.NONE
            )
        }

    @Test
    fun `deletePlacedCard when lock has wrong format then validation fails`() =
        testPlacedCardLockMatchesFormatValidation(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = PlacedCard(
                id = PlacedCardId(GOOD_ID),
                lock = FcPlacedCardLock(BAD_NOT_EMPTY_ID)
            )
        }

}