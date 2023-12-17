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

class RepositoryDeleteCardTest {

    private val command = PlacedCardCommand.DELETE_PLACED_CARD
    private val uuid = "10000000-0000-0000-0000-000000000001"

    private val initPlacedCard = PlacedCard(
        id = PlacedCardId(uuid),
        lock = FcPlacedCardLock("123-234-abc-ABC")
    )

    private val repository = MockPlacedCardRepository(
        invokeRead = { PlacedCardDbResponse.success(initPlacedCard) },
        invokeDelete = { PlacedCardDbResponse.SUCCESS_EMPTY }
    )

    private val repositoryConfig = PlacedCardRepositoryConfig(testRepository = repository)
    private val corConfig = PlacedCardsCorConfig(repositoryConfig)
    private val processor = FcPlacedCardProcessor(corConfig)

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
        processor.exec(context)
        assertEquals(FcState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(PlacedCard.EMPTY, context.responsePlacedCard)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundByIdTest(command)
}


