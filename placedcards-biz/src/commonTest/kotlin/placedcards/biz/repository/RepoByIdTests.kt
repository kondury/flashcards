package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.biz.common.setAdminPrincipal
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals


private val initCard = PlacedCard(
    id = PlacedCardId("123"),
)

private val processor by lazy {
    val repository = initSingleMockRepository(initCard)
    initProcessor(repository)
}

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
    context.setAdminPrincipal()
    processor.exec(context)
    with (context) {
        assertEquals(FcState.FAILING, state)
        assertEquals(1, errors.size)
        assertEquals("id", errors.first().field)
        assertEquals(PlacedCard.EMPTY, responsePlacedCard)
    }
}
