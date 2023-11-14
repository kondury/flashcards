package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

internal fun runSuccessfulValidationTest(
    processor: FcCardProcessor,
    command: CardCommand,
    configureContext: CardContext.() -> Unit,
    assertSpecific: (CardContext) -> Unit
) = runValidationTest(processor, command, configureContext) { context ->
    assertEquals(FcState.FINISHING, context.state)
    assertTrue(context.validatedCard.isNotEmpty())
    assertTrue(context.errors.isEmpty())
    assertSpecific(context)
}

internal fun runSingleErrorValidationTest(
    processor: FcCardProcessor,
    command: CardCommand,
    configureContext: CardContext.() -> Unit,
    assertError: (FcError) -> Unit
) = runValidationTest(processor, command, configureContext) { context ->
    assertEquals(FcState.FAILING, context.state)
    assertTrue(context.validatedCard.isEmpty())

    val error = context.errors.firstOrNull() ?: fail("context.errors are expected to contain one error")
    assertEquals("validation", error.group)
    assertEquals(FcError.Level.INFO, error.level)
    assertError(error)
}

internal fun runValidationTest(
    processor: FcCardProcessor,
    command: CardCommand,
    configureContext: CardContext.() -> Unit,
    assertions: (CardContext) -> Unit
) = runTest {
    val context = CardContext(
        command = command,
        workMode = FcWorkMode.TEST,
    ).apply(configureContext)
    processor.exec(context)
    assertions(context)
}

