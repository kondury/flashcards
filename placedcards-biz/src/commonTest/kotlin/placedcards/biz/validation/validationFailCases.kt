package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlin.test.assertEquals

internal fun testSearchStrategyIsNotEmptyValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-searchStrategy-empty", error.code)
        assertEquals("searchStrategy", error.field)
    }

internal fun testBoxIsNotEmptyValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-box-empty", error.code)
        assertEquals("box", error.field)
    }

internal fun testOwnerIdIsNotEmptyValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext
    ) { error ->
        assertEquals("validation-${command.name.lowercase()}-ownerId-empty", error.code)
        assertEquals("ownerId", error.field)
    }

internal fun testOwnerIdMatchesFormatValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = configureContext
    ) { error ->
        assertEquals("validation-${command.name.lowercase()}-ownerId-badFormat", error.code)
        assertEquals("ownerId", error.field)
    }


internal fun testPlacedCardIdIsNotEmptyValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-placedCardId-empty", error.code)
        assertEquals("placedCardId", error.field)
    }

internal fun testCardIdMatchesFormatValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = configureContext
    ) { error ->
        assertEquals("validation-${command.name.lowercase()}-cardId-badFormat", error.code)
        assertEquals("cardId", error.field)
    }

internal fun testCardIdIsNotEmptyValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-cardId-empty", error.code)
        assertEquals("cardId", error.field)
    }

internal fun testPlacedCardIdMatchesFormatValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-placedCardId-badFormat", error.code)
        assertEquals("placedCardId", error.field)
    }

internal fun testPlacedCardLockIsNotEmptyValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-lock-empty", error.code)
        assertEquals("lock", error.field)
    }

internal fun testPlacedCardLockMatchesFormatValidation(
    processor: FcPlacedCardProcessor,
    command: PlacedCardCommand,
    configureContext: PlacedCardContext.() -> Unit
) =
    runSingleErrorValidationTest(processor, command, configureContext) { error ->
        assertEquals("validation-${command.name.lowercase()}-lock-badFormat", error.code)
        assertEquals("lock", error.field)
    }

