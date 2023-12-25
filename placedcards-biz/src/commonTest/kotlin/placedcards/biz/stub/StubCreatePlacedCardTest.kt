package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
import kotlin.test.Test
import kotlin.test.assertEquals


class StubCreatePlacedCardTest {

    companion object {
        private val processor = initProcessor(PlacedCardRepository.NoOpPlacedCardRepository)
        private val placedCardId = PlacedCardId("100")
        private val expectedOwnerId = UserId("user-id")
        private val expectedBox = FcBox.NEW
        private val expectedCardId = CardId("card-id")
    }

    @Test
    fun createPlacedCardSuccess() =
        runSuccessStubTest(
            processor = processor,
            command = PlacedCardCommand.CREATE_PLACED_CARD,
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
    fun wrongOwnerIdError() = testWrongOwnerIdErrorStub(processor, PlacedCardCommand.CREATE_PLACED_CARD)

    @Test
    fun wrongCardIdError() = testWrongCardIdErrorStub(processor, PlacedCardCommand.CREATE_PLACED_CARD)

    @Test
    fun wrongBoxError() = testWrongBoxErrorStub(processor, PlacedCardCommand.CREATE_PLACED_CARD)

    @Test
    fun databaseError() = testDatabaseErrorStub(processor, PlacedCardCommand.CREATE_PLACED_CARD)

    @Test
    fun stubNoCaseError() = testNoCaseStubError(processor, PlacedCardCommand.CREATE_PLACED_CARD)
}