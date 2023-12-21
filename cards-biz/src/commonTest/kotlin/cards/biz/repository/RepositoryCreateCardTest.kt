package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.common.initProcessor
import com.github.kondury.flashcards.cards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class RepositoryCreateCardTest {

    private val uuid = "10000000-0000-0000-0000-000000000001"

    private val processor by lazy {
        val repository = initSingleMockRepository(newUuid = uuid)
        initProcessor(repository)
    }

    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = CardContext(
            command = CardCommand.CREATE_CARD,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestCard = Card(
                front = "front",
                back = "back",
            ),
        )
        processor.exec(context)
        assertEquals(FcState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertNotEquals(CardId.NONE, context.responseCard.id)
        assertEquals("front", context.responseCard.front)
        assertEquals("back", context.responseCard.back)
    }
}
