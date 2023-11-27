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
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class RepositoryCreateCardTest {

    private val command = CardCommand.CREATE_CARD
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repository = MockCardRepository(
        invokeCreate = {
            CardDbResponse.success(
                Card(
                    id = CardId(uuid),
                    front = it.card.front,
                    back = it.card.back
                )
            )
        }
    )

    private val repositoryConfig = CardRepositoryConfig(testRepository = repository)
    private val corConfig = CardsCorConfig(repositoryConfig)
    private val processor = FcCardProcessor(corConfig)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = CardContext(
            command = command,
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
