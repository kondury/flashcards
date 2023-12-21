package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.biz.common.setAdminPrincipal
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
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

    private val processor by lazy {
        val repository = initSingleMockRepository(initPlacedCard, lockAfter.asString())
        initProcessor(repository)
    }

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
        context.setAdminPrincipal()
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


