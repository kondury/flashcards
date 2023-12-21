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


class RepositoryDeleteCardTest {

    private val command = PlacedCardCommand.DELETE_PLACED_CARD
    private val uuid = "10000000-0000-0000-0000-000000000001"

    private val initPlacedCard = PlacedCard(
        id = PlacedCardId(uuid),
        lock = FcPlacedCardLock("123-234-abc-ABC")
    )

    private val processor by lazy {
        val repository = initSingleMockRepository(initPlacedCard)
        initProcessor(repository)
    }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val cardToDelete = PlacedCard(
            id = PlacedCardId(uuid),
            lock = FcPlacedCardLock("123-234-abc-ABC"),
        )
        val context = PlacedCardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestPlacedCard = cardToDelete,
        )
        context.setAdminPrincipal()
        processor.exec(context)
        assertEquals(FcState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(PlacedCard.EMPTY, context.responsePlacedCard)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundByIdTest(command)
}


