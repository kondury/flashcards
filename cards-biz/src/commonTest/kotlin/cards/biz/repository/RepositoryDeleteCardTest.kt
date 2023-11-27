package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.repository.tests.MockCardRepository
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
        lock = FcCardLock("123-234-abc-ABC"), // todo change lock id
    )

    private val repository by lazy {
        MockCardRepository(
            invokeRead = { CardDbResponse.success(initCard) },
            invokeDelete = { CardDbResponse.SUCCESS_EMPTY }
        )
    }
    private val repositoryConfig by lazy { CardRepositoryConfig(testRepository = repository) }
    private val corConfig by lazy { CardsCorConfig(repositoryConfig) }
    private val processor by lazy { FcCardProcessor(corConfig) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val cardToDelete = Card(
            id = CardId("123"),
            lock = FcCardLock("123-234-abc-ABC"), // todo change lock id
        )
        val context = CardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestCard = cardToDelete,
        )
        processor.exec(context)
        assertEquals(FcState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(Card.EMPTY, context.responseCard)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}


