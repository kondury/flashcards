package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.common.initProcessor
import com.github.kondury.flashcards.cards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

private val initCard = Card(
    id = CardId("123"),
    front = "Front",
    back = "Back",
)

private val processor by lazy {
    val repository = initSingleMockRepository(initCard)
    initProcessor(repository)
}

fun repoNotFoundTest(command: CardCommand) = runTest {
    val context = CardContext(
        command = command,
        state = FcState.NONE,
        workMode = FcWorkMode.TEST,
        requestCard = Card(
            id = CardId("12345"),
            front = "Front",
            back = "Back",
            lock = FcCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(context)
    assertEquals(FcState.FAILING, context.state)
    assertEquals(1, context.errors.size)
    assertEquals("id", context.errors.first().field)
    assertEquals(Card.EMPTY, context.responseCard)
}
