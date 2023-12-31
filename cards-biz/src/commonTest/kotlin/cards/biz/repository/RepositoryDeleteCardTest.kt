package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.common.initProcessor
import com.github.kondury.flashcards.cards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.cards.biz.common.setAdminPrincipal
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RepositoryDeleteCardTest {

    private val command = CardCommand.DELETE_CARD
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
    fun repoDeleteSuccessTest() = runTest {
        val cardToDelete = Card(
            id = CardId("123"),
            lock = FcCardLock("123-234-abc-ABC"),
        )
        val context = CardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestCard = cardToDelete,
        )
        context.setAdminPrincipal()
        processor.exec(context)
        assertEquals(FcState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(Card.EMPTY, context.responseCard)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}


