package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.stubs.FcStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

internal fun testSuccessStub(
    processor: FcCardProcessor,
    command: CardCommand,
    requestCard: Card,
    assertSuccessSpecific: (CardContext) -> Unit
) = runStubTest(processor, command, FcStub.SUCCESS, requestCard) { context ->
    assertEquals(FcState.FINISHING, context.state)
    assertIs<MutableList<FcError>>(context.errors).isEmpty()
    assertSuccessSpecific(context)
}

internal fun testWrongCardIdErrorStub(processor: FcCardProcessor, command: CardCommand, requestCard: Card) =
    runErrorStubTest(processor, command, FcStub.WRONG_CARD_ID, requestCard) { error ->
        with(error) {
            assertEquals("validation", group)
            assertEquals("validation-id", code)
            assertEquals("id", field)
        }
    }

internal fun testWrongFrontSideErrorStub(processor: FcCardProcessor, command: CardCommand, requestCard: Card) =
    runErrorStubTest(processor, command, FcStub.WRONG_FRONT_SIDE, requestCard) { error ->
        with(error) {
            assertEquals("validation", group)
            assertEquals("validation-front", code)
            assertEquals("front", field)
        }
    }

internal fun testWrongBackSideErrorStub(processor: FcCardProcessor, command: CardCommand, requestCard: Card) =
    runErrorStubTest(processor, command, FcStub.WRONG_BACK_SIDE, requestCard) { error ->
        with(error) {
            assertEquals("validation", group)
            assertEquals("validation-back", code)
            assertEquals("back", field)
        }
    }

internal fun testNotFoundStubError(processor: FcCardProcessor, command: CardCommand, requestCard: Card) =
    runErrorStubTest(processor, command, FcStub.NOT_FOUND, requestCard) { error ->
        with(error) {
            assertEquals("db-error", group)
            assertEquals("db-error-card-not-found", code)
            assertEquals("", field)
        }
    }

internal fun testDatabaseErrorStub(processor: FcCardProcessor, command: CardCommand, requestCard: Card) =
    runErrorStubTest(processor, command, FcStub.DB_ERROR, requestCard) { error ->
        with(error) {
            assertEquals("db-error", group)
            assertEquals("db-error-general", code)
            assertEquals("", field)
        }
    }

internal fun testNoCaseStubError(processor: FcCardProcessor, command: CardCommand, requestCard: Card) =
    runErrorStubTest(processor, command, FcStub.NONE, requestCard) { error ->
        with(error) {
            assertEquals("stub-error", group)
            assertEquals("unsupported-case-stub", code)
            assertEquals("stub", field)
        }
    }

private fun runErrorStubTest(
    processor: FcCardProcessor,
    command: CardCommand,
    stub: FcStub,
    requestCard: Card,
    assertError: (FcError) -> Unit
) = runStubTest(processor, command, stub, requestCard) { context ->
    with(context) {
        assertEquals(Card(), responseCard)
        assertEquals(FcState.FAILING, state)
        assertEquals(1, errors.size)
    }
    val error = context.errors.firstOrNull() ?: fail("context.errors expected to contain a single error")
    assertError(error)
}

private fun runStubTest(
    processor: FcCardProcessor,
    command: CardCommand,
    stub: FcStub,
    requestCard: Card,
    assertions: (CardContext) -> Unit
) = runTest {
    val context = CardContext(
        command = command,
        state = FcState.NONE,
        workMode = FcWorkMode.STUB,
        stubCase = stub,
        requestCard = requestCard
    )
    processor.exec(context)
    assertions(context)
}

