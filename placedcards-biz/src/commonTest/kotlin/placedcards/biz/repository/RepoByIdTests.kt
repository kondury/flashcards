package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse
import com.github.kondury.flashcards.placedcards.repository.tests.MockPlacedCardRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

private val initCard = PlacedCard(
    id = PlacedCardId("123"),
)

private val repository by lazy {
    MockPlacedCardRepository(
        invokeRead = {
            if (it.id == initCard.id) PlacedCardDbResponse.success(initCard)
            else PlacedCardDbResponse.error(
                FcError(
                    message = "Not found",
                    field = "id"
                )
            )
        }
    )
}
private val repositoryConfig by lazy { PlacedCardRepositoryConfig(testRepository = repository) }
private val corConfig by lazy { PlacedCardsCorConfig(repositoryConfig) }
private val processor by lazy { FcPlacedCardProcessor(corConfig) }

fun repoNotFoundByIdTest(command: PlacedCardCommand) = runTest {
    val context = PlacedCardContext(
        command = command,
        state = FcState.NONE,
        workMode = FcWorkMode.TEST,
        requestPlacedCard = PlacedCard(
            id = PlacedCardId("12345"),
            lock = FcPlacedCardLock("123-234-abc-ABC"),
            box = FcBox.REPEAT
        ),
    )
    processor.exec(context)
    with (context) {
        assertEquals(FcState.FAILING, state)
        assertEquals(1, errors.size)
        assertEquals("id", errors.first().field)
        assertEquals(PlacedCard.EMPTY, responsePlacedCard)
    }
}
