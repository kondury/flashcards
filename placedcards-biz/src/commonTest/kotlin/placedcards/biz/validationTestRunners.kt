package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

internal fun runSuccessfulValidationTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configure: PlacedCardContext.() -> Unit,
    assertSpecific: (PlacedCardContext) -> Unit
) = runValidationTest(
    processor = processor,
    command = command,
    configure = configure,
) { context ->
    assertEquals(FcState.FINISHING, context.state)
    assertTrue(context.errors.isEmpty())
    assertSpecific(context)
}

internal fun runSingleErrorValidationTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configure: PlacedCardContext.() -> Unit = {},
    assertError: (FcError) -> Unit
) = runValidationTest(
    processor,
    command,
    configure = configure
) { context ->
    assertEquals(FcState.FAILING, context.state)
    assertTrue(context.validatedPlacedCard.isEmpty())

    val error = context.errors.firstOrNull() ?: fail("context.errors are expected to contain one error")
    assertEquals("validation", error.group)
    assertEquals(FcError.Level.INFO, error.level)
    assertError(error)
}

internal fun runValidationTest(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configure: PlacedCardContext.() -> Unit = {},
    assertions: (PlacedCardContext) -> Unit
) = runTest {
    val context = PlacedCardContext(
        command = command,
        workMode = FcWorkMode.TEST
    ).apply(configure)
    processor.exec(context)
    assertions(context)
}

