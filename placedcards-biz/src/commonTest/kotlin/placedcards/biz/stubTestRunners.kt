package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.fail

internal fun runSuccessStubTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    requestPlacedCard: PlacedCard = PlacedCard.EMPTY,
    requestPlacedCardId: PlacedCardId = PlacedCardId.NONE,
    requestOwnerId: UserId = UserId.NONE,
    requestWorkBox: FcBox = FcBox.NONE,
    requestBoxAfter: FcBox = FcBox.NONE,
    requestSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,
    assertSuccessSpecific: (PlacedCardContext) -> Unit
) = runStubTest(
    processor = processor,
    command = command,
    stub = FcStub.SUCCESS,
    requestPlacedCard = requestPlacedCard,
    requestPlacedCardId = requestPlacedCardId,
    requestOwnerId = requestOwnerId,
    requestWorkBox = requestWorkBox,
    requestBoxAfter = requestBoxAfter,
    requestSearchStrategy = requestSearchStrategy
) { context ->
    assertEquals(FcState.FINISHING, context.state)
    assertIs<MutableList<FcError>>(context.errors).isEmpty()
    assertSuccessSpecific(context)
}

internal fun runErrorStubTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    stub: FcStub,
    assertError: (FcError) -> Unit
) = runStubTest(
    processor = processor,
    command = command,
    stub = stub,
) { context ->
    with(context) {
        assertTrue(responsePlacedCard.isEmpty())
        assertEquals(FcState.FAILING, state)
        assertEquals(1, errors.size)
    }
    val error = context.errors.firstOrNull() ?: fail("context.errors expected to contain a single error")
    assertError(error)
}

internal fun runStubTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    stub: FcStub,
    requestPlacedCard: PlacedCard = PlacedCard.EMPTY,
    requestPlacedCardId: PlacedCardId = PlacedCardId.NONE,
    requestOwnerId: UserId = UserId.NONE,
    requestWorkBox: FcBox = FcBox.NONE,
    requestBoxAfter: FcBox = FcBox.NONE,
    requestSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,
    assertions: (PlacedCardContext) -> Unit
) = runTest {
    val context = PlacedCardContext(
        command = command,
        state = FcState.NONE,
        workMode = FcWorkMode.STUB,
        stubCase = stub,
        requestPlacedCard = requestPlacedCard,
        requestPlacedCardId = requestPlacedCardId,
        requestOwnerId = requestOwnerId,
        requestWorkBox = requestWorkBox,
        requestBoxAfter = requestBoxAfter,
        requestSearchStrategy = requestSearchStrategy,
    )
    processor.exec(context)
    assertions(context)
}
