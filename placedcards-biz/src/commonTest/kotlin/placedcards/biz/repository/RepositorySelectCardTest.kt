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

class RepositorySelectCardTest {

    private val command = PlacedCardCommand.SELECT_PLACED_CARD

    private val ownerId = UserId("user-id-1")
    private val workBox = FcBox.REPEAT
    private val searchStrategy = FcSearchStrategy.EARLIEST_CREATED

    private val expectedPlacedCard = PlacedCard(
        id = PlacedCardId("good-placed-card-id"),
        ownerId = ownerId,
        cardId = CardId("card-id-1"),
        box = workBox,
    )

    private val repository = MockPlacedCardRepository(
        invokeSelect = { PlacedCardDbResponse.success(expectedPlacedCard) }
    )

    private val repositoryConfig = PlacedCardRepositoryConfig(testRepository = repository)
    private val corConfig = PlacedCardsCorConfig(repositoryConfig)
    private val processor = FcPlacedCardProcessor(corConfig)

    @Test
    fun repoSelectSuccessTest() = runTest {
        val context = PlacedCardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestOwnerId = ownerId,
            requestWorkBox = workBox,
            requestSearchStrategy = searchStrategy,
        )
        processor.exec(context)
        with(context) {
            assertEquals(FcState.FINISHING, state)
            assertTrue { errors.isEmpty() }
            assertSame(expectedPlacedCard, responsePlacedCard)
        }
    }
}
