package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cards.common.stubs.FcStub
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class FromCardTransportTest {

    @Test
    fun `test fromCardCreateRequest mapping`() {
        val request = CardCreateRequest(
            requestId = "CreateRequestId",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            card = CardCreateResource(
                front = "Front text",
                back = "Back text",
            )
        )

        val context = CardContext()
        context.fromCardCreateRequest(request)

        assertCommon(
            expectedCommand = CardCommand.CREATE_CARD,
            expectedStub = FcStub.SUCCESS,
            expectedMode = FcWorkMode.STUB,
            expectedRequestId = "CreateRequestId",
            actual = context
        )

        Assertions.assertEquals("Front text", context.requestCard.front)
        Assertions.assertEquals("Back text", context.requestCard.back)
    }

    @Test
    fun `test fromCardDeleteRequest mapping`() {
        val request = CardDeleteRequest(
            requestId = "DeleteRequestId",
            debug = DebugResource(
                mode = RunMode.TEST,
            ),
            card = CardDeleteResource(id = "CardId")
        )

        val context = CardContext()
        context.fromCardDeleteRequest(request)

        assertCommon(
            expectedCommand = CardCommand.DELETE_CARD,
            expectedStub = FcStub.NONE,
            expectedMode = FcWorkMode.TEST,
            expectedRequestId = "DeleteRequestId",
            actual = context
        )
        Assertions.assertEquals("CardId", context.requestCard.id.asString())
    }

    @Test
    fun `test fromCardReadRequest mapping`() {
        val request = CardReadRequest(
            requestId = "ReadRequestId",
            debug = DebugResource(
                mode = RunMode.TEST,
            ),
            card = CardReadResource(id = "CardId")
        )

        val context = CardContext()
        context.fromCardReadRequest(request)

        assertCommon(
            expectedCommand = CardCommand.READ_CARD,
            expectedStub = FcStub.NONE,
            expectedMode = FcWorkMode.TEST,
            expectedRequestId = "ReadRequestId",
            actual = context
        )
        Assertions.assertEquals("CardId", context.requestCard.id.asString())
    }

    private fun assertCommon(
        expectedCommand: CardCommand,
        expectedStub: FcStub,
        expectedMode: FcWorkMode,
        expectedRequestId: String,
        actual: CardContext
    ) {
        Assertions.assertEquals(expectedStub, actual.stubCase)
        Assertions.assertEquals(expectedMode, actual.workMode)
        Assertions.assertEquals(expectedRequestId, actual.requestId.asString())
        Assertions.assertEquals(expectedCommand, actual.command)
    }
}