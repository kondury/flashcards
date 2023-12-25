package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.common.initProcessor
import com.github.kondury.flashcards.cards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RepositoryReadCardTest {

    private val command = CardCommand.READ_CARD
    private val initCard = Card(
        id = CardId("123"),
        front = "Front",
        back = "Back",
        lock = FcCardLock("123-234-abc-ABC"),
    )

    private val processor by lazy {
        val repository = initSingleMockRepository(initCard)
        initProcessor(repository)
    }

    @Test
    fun repoReadSuccessTest() = runTest {
        val context = CardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestCard = Card(
                id = initCard.id,
            ),
        )
        processor.exec(context)
        assertEquals(FcState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(initCard, context.responseCard)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
