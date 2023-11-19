package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.CREATE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
import kotlin.test.Test
import kotlin.test.assertEquals


class StubCreatePlacedCardTest {

    companion object {
        private val processor = FcPlacedCardProcessor()

        private val placedCardId = PlacedCardId("100")
        private val expectedOwnerId = UserId("user-id")
        private val expectedBox = FcBox.NEW
        private val expectedCardId = CardId("card-id")
    }

    @Test
    fun createPlacedCardSuccess() =
        runSuccessStubTest(
            processor = processor,
            command = CREATE_PLACED_CARD,
            configureContext = {
                requestPlacedCard = PlacedCard(
                    id = placedCardId,
                    ownerId = expectedOwnerId,
                    box = expectedBox,
                    cardId = expectedCardId
                )
            }
        ) { context ->
            with(context.responsePlacedCard) {
                assertEquals(PlacedCardStub.get().id, id)
                assertEquals(expectedOwnerId, ownerId)
                assertEquals(expectedBox, box)
                assertEquals(expectedCardId, cardId)
            }
        }

    @Test
    fun wrongOwnerIdError() = testWrongOwnerIdErrorStub(processor, CREATE_PLACED_CARD)

    @Test
    fun wrongCardIdError() = testWrongCardIdErrorStub(processor, CREATE_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, CREATE_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, CREATE_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, CREATE_PLACED_CARD)
}