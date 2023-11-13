package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FromPlacedCardTransportTest {

    @Test
    fun `test fromPlacedCardCreateRequest mapping`() {
        val request = PlacedCardCreateRequest(
            requestId = "CreateRequestId",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            placedCard = PlacedCardCreateResource(
                box = Box.NEW,
                ownerId = "OwnerId",
                cardId = "CardId"
            )
        )

        val context = PlacedCardContext()
        context.fromPlacedCardCreateRequest(request)

        assertCommon(
            expectedCommand = PlacedCardCommand.CREATE_PLACED_CARD,
            expectedStub = FcStub.SUCCESS,
            expectedMode = FcWorkMode.STUB,
            expectedRequestId = "CreateRequestId",
            actual = context
        )

        assertEquals(FcBox.NEW, context.requestPlacedCard.box)
        assertEquals("OwnerId", context.requestPlacedCard.ownerId.asString())
        assertEquals("CardId", context.requestPlacedCard.cardId.asString())
    }

    @Test
    fun `test fromPlacedCardDeleteRequest mapping`() {
        val request = PlacedCardDeleteRequest(
            requestId = "DeleteRequestId",
            debug = DebugResource(
                mode = RunMode.PROD,
            ),
            placedCard = PlacedCardDeleteResource(
                id = "PlacedCardId"
            )
        )

        val context = PlacedCardContext()
        context.fromPlacedCardDeleteRequest(request)

        assertCommon(
            expectedCommand = PlacedCardCommand.DELETE_PLACED_CARD,
            expectedStub = FcStub.NONE,
            expectedMode = FcWorkMode.PROD,
            expectedRequestId = "DeleteRequestId",
            actual = context
        )

        assertEquals("PlacedCardId", context.requestPlacedCardId.asString())
    }

    @Test
    fun `test fromPlacedCardMoveRequest mapping`() {
        val request = PlacedCardMoveRequest(
            requestId = "MoveRequestId",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            move = PlacedCardMoveResource(
                id = "PlacedCardId",
                box = Box.REPEAT,
            )
        )

        val context = PlacedCardContext()
        context.fromPlacedCardMoveRequest(request)

        assertCommon(
            expectedCommand = PlacedCardCommand.MOVE_PLACED_CARD,
            expectedStub = FcStub.SUCCESS,
            expectedMode = FcWorkMode.STUB,
            expectedRequestId = "MoveRequestId",
            actual = context
        )

        assertEquals(FcBox.REPEAT, context.requestBoxAfter)
        assertEquals("PlacedCardId", context.requestPlacedCardId.asString())
    }

    @Test
    fun `test fromPlacedCardInitRequest mapping`() {
        val request = PlacedCardInitRequest(
            requestId = "InitRequestId",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            init = PlacedCardInitResource(
                box = Box.NEW,
                ownerId = "OwnerId",
            )
        )

        val context = PlacedCardContext()
        context.fromPlacedCardInitRequest(request)

        assertCommon(
            expectedCommand = PlacedCardCommand.INIT_PLACED_CARD,
            expectedStub = FcStub.SUCCESS,
            expectedMode = FcWorkMode.STUB,
            expectedRequestId = "InitRequestId",
            actual = context
        )

        assertEquals(FcBox.NEW, context.requestWorkBox)
        assertEquals("OwnerId", context.requestOwnerId.asString())
    }

    @Test
    fun `test fromPlacedCardSelectRequest mapping`() {
        val request = PlacedCardSelectRequest(
            requestId = "SelectRequestId",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            select = PlacedCardSelectResource(
                box = Box.NEW,
                ownerId = "OwnerId",
                searchStrategy = SearchStrategy.EARLIEST_CREATED
            )
        )

        val context = PlacedCardContext()
        context.fromPlacedCardSelectRequest(request)

        assertCommon(
            expectedCommand = PlacedCardCommand.SELECT_PLACED_CARD,
            expectedStub = FcStub.SUCCESS,
            expectedMode = FcWorkMode.STUB,
            expectedRequestId = "SelectRequestId",
            actual = context
        )

        assertEquals(FcBox.NEW, context.requestWorkBox)
        assertEquals("OwnerId", context.requestOwnerId.asString())
        assertEquals(FcSearchStrategy.EARLIEST_CREATED, context.requestSearchStrategy)
    }

    private fun assertCommon(
        expectedCommand: PlacedCardCommand,
        expectedStub: FcStub,
        expectedMode: FcWorkMode,
        expectedRequestId: String,
        actual: PlacedCardContext
    ) {
        assertEquals(expectedStub, actual.stubCase)
        assertEquals(expectedMode, actual.workMode)
        assertEquals(expectedRequestId, actual.requestId.asString())
        assertEquals(expectedCommand, actual.command)
    }
}