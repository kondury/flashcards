package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.repository.tests.MockCardRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

private val initCard = Card(
    id = CardId("123"),
    front = "Front",
    back = "Back",
)

private val repository by lazy {
    MockCardRepository(
        invokeRead = {
            if (it.id == initCard.id) CardDbResponse.success(initCard)
            else CardDbResponse.error(
                FcError(
                    message = "Not found",
                    field = "id"
                )
            )
        }
    )
}
private val repositoryConfig by lazy { CardRepositoryConfig(testRepository = repository) }
private val corConfig by lazy { CardsCorConfig(repositoryConfig) }
private val processor by lazy { FcCardProcessor(corConfig) }

fun repoNotFoundTest(command: CardCommand) = runTest {
    val context = CardContext(
        command = command,
        state = FcState.NONE,
        workMode = FcWorkMode.TEST,
        requestCard = Card(
            id = CardId("12345"),
            front = "Front",
            back = "Back",
            lock = FcCardLock("123-234-abc-ABC"), // todo change lock id
        ),
    )
    processor.exec(context)
    assertEquals(FcState.FAILING, context.state)
    assertEquals(1, context.errors.size)
    assertEquals("id", context.errors.first().field)
    assertEquals(Card.EMPTY, context.responseCard)
}
