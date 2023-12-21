package com.github.kondury.flashcards.placedcards.biz.permissions

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


internal fun runPermissionSuccessTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configure: PlacedCardContext.() -> Unit,
) = runPermissionTest(processor, command, configure) { context ->
    assertEquals(FcState.FINISHING, context.state)
    assertTrue(context.isPermitted, "context.isPermitted is expected to be true")
    assertTrue { context.errors.isEmpty() }
    assertNotNull(context.responsePlacedCard)
}

internal fun runPermissionFailTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configure: PlacedCardContext.() -> Unit,
) = runPermissionTest(processor, command, configure) { context ->
    assertEquals(FcState.FAILING, context.state)
    assertEquals(1, context.errors.size)
    assertFalse(context.isPermitted, "context.isPermitted is expected to be false")
    assertTrue { "is not allowed to perform" in context.errors.first().message }
    assertEquals(PlacedCard.EMPTY, context.responsePlacedCard)
}

internal fun runPermissionTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configure: PlacedCardContext.() -> Unit,
    assertions: (PlacedCardContext) -> Unit
) = runTest {

    val context = PlacedCardContext(
        command = command,
        state = FcState.NONE,
        workMode = FcWorkMode.TEST,
    ).apply(configure)

    processor.exec(context)
    assertions(context)
}

