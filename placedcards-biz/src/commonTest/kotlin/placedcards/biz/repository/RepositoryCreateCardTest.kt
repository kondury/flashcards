package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse
import com.github.kondury.flashcards.placedcards.repository.tests.MockPlacedCardRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class RepositoryCreateCardTest {

    private val command = PlacedCardCommand.CREATE_PLACED_CARD

    private val expectedUserId = UserId("user-id-1")
    private val expectedBox = FcBox.NEW
    private val expectedCardId = CardId("card-id-1")
    private val expectedPlacedCard = PlacedCard(
        id = PlacedCardId("placed-card-id-1"),
        ownerId = expectedUserId,
        cardId = expectedCardId,
        box = expectedBox,
    )

    private val repository = MockPlacedCardRepository(
        invokeCreate = { PlacedCardDbResponse.success(expectedPlacedCard) }
    )

    private val repositoryConfig = PlacedCardRepositoryConfig(testRepository = repository)
    private val corConfig = PlacedCardsCorConfig(repositoryConfig)
    private val processor = FcPlacedCardProcessor(corConfig)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = PlacedCardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestPlacedCard = PlacedCard(
                ownerId = expectedUserId,
                box = expectedBox,
                cardId = expectedCardId
            ),
        )
        processor.exec(context)
        with (context) {
            assertEquals(FcState.FINISHING, state)
            assertTrue { errors.isEmpty() }
            assertSame(expectedPlacedCard, responsePlacedCard)
        }

    }
}
