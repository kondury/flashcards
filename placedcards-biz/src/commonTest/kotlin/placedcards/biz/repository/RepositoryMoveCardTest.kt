package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse
import com.github.kondury.flashcards.placedcards.repository.tests.MockPlacedCardRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RepositoryMoveCardTest {

    private val command = PlacedCardCommand.MOVE_PLACED_CARD
    private val boxBefore = FcBox.NEW
    private val boxAfter = FcBox.REPEAT
    private val lockBefore = FcPlacedCardLock("111-222-abc-ABC")
    private val lockAfter = FcPlacedCardLock("999-888-xyz-XYZ")

    private val initPlacedCard = PlacedCard(
        id = PlacedCardId("good-placed-card-id"),
        ownerId = UserId("good-user-id"),
        cardId = CardId("good-card-id"),
        box = boxBefore,
        lock = lockBefore,
    )

    private val movedPlacedCard = initPlacedCard.copy(
        box = boxAfter,
        lock = lockAfter
    )

    private val repository = MockPlacedCardRepository(
        invokeRead = { PlacedCardDbResponse.success(initPlacedCard) },
        invokeMove = { PlacedCardDbResponse.success(initPlacedCard.copy(box = it.box, lock = lockAfter)) }
    )

    private val repositoryConfig = PlacedCardRepositoryConfig(testRepository = repository)
    private val corConfig = PlacedCardsCorConfig(repositoryConfig)
    private val processor = FcPlacedCardProcessor(corConfig)

    @Test
    fun repoMoveSuccessTest() = runTest {
        val goodMoveRequest = PlacedCard(
            id = initPlacedCard.id,
            box = boxAfter,
            lock = initPlacedCard.lock
        )

        val context = PlacedCardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestPlacedCard = goodMoveRequest,
        )

        processor.exec(context)
        with(context) {
            assertEquals(FcState.FINISHING, state)
            assertTrue { errors.isEmpty() }
            assertEquals(movedPlacedCard, responsePlacedCard)
        }
    }

    @Test
    fun repoMoveNotFoundTest() = repoNotFoundByIdTest(command)
}


