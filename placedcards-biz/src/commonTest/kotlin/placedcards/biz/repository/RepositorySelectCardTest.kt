package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.biz.common.setAdminPrincipal
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue


class RepositorySelectCardTest {

    private val command = PlacedCardCommand.SELECT_PLACED_CARD

    private val ownerId = UserId("user-id-1")
    private val workBox = FcBox.REPEAT
    private val searchStrategy = FcSearchStrategy.EARLIEST_CREATED

    private val expectedPlacedCard = PlacedCard(
        id = PlacedCardId("good-placed-card-id"),
        ownerId = ownerId,
        cardId = CardId("card-id-1"),
        box = workBox,
    )

    private val processor by lazy {
        val repository = initSingleMockRepository(expectedPlacedCard)
        initProcessor(repository)
    }

    @Test
    fun repoSelectSuccessTest() = runTest {
        val context = PlacedCardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestOwnerId = ownerId,
            requestWorkBox = workBox,
            requestSearchStrategy = searchStrategy,
        )
        context.setAdminPrincipal()
        processor.exec(context)
        with(context) {
            assertEquals(FcState.FINISHING, state)
            assertTrue { errors.isEmpty() }
            assertSame(expectedPlacedCard, responsePlacedCard)
        }
    }
}
