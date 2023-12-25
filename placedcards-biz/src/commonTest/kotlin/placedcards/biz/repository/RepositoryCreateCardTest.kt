package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.common.NONE
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.*


class RepositoryCreateCardTest {

    private val command = PlacedCardCommand.CREATE_PLACED_CARD

    private val newUuid = "999-888-xyz-XYZ"
    private val expectedUserId = UserId("user-id-1")
    private val expectedBox = FcBox.NEW
    private val expectedCardId = CardId("card-id-1")

    private val processor by lazy {
        val repository = initSingleMockRepository(newUuid = newUuid)
        initProcessor(repository)
    }

    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = PlacedCardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestPlacedCard = PlacedCard(
                ownerId = expectedUserId,
                box = expectedBox,
                cardId = expectedCardId
            ),
        )
        processor.exec(context)
        with (context) {
            assertEquals(FcState.FINISHING, state)
            assertTrue { errors.isEmpty() }
            assertNotEquals(PlacedCardId.NONE, responsePlacedCard.id)
            assertNotEquals(FcPlacedCardLock.NONE, responsePlacedCard.lock)
            assertEquals(expectedUserId, responsePlacedCard.ownerId)
            assertEquals(expectedBox, responsePlacedCard.box)
            assertEquals(expectedCardId, requestPlacedCard.cardId)
            assertNotEquals(Instant.NONE, responsePlacedCard.createdAt)
            assertNotEquals(Instant.NONE, responsePlacedCard.updatedAt)
        }

    }
}
