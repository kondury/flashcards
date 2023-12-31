package com.github.kondury.flashcards.cards.biz.validation

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import kotlin.test.assertEquals

private const val SOME_EMPTY_STRING = " \t \t"
private const val GOOD_NOT_EMPTY_ID = "123-abs-ZX-"
private const val BAD_NOT_EMPTY_ID = ")>$GOOD_NOT_EMPTY_ID"
private const val GOOD_FRONT = "Front text"
private const val GOOD_BACK = "Back text"

internal fun testCardIdIsEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId(GOOD_NOT_EMPTY_ID),
                front = GOOD_FRONT,
                back = GOOD_BACK
            )
        }
    ) { error ->
        assertEquals("validation-id-not-empty", error.code)
        assertEquals("id", error.field)
    }

internal fun testFrontIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId.NONE,
                front = SOME_EMPTY_STRING,
                back = GOOD_BACK
            )
        }
    ) { error ->
        assertEquals("validation-front-empty", error.code)
        assertEquals("front", error.field)
    }

internal fun testBackIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId.NONE,
                front = GOOD_FRONT,
                back = SOME_EMPTY_STRING
            )
        },
    ) { error ->
        assertEquals("validation-back-empty", error.code)
        assertEquals("back", error.field)
    }

internal fun testCardIdIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId.NONE,
                lock = FcCardLock(GOOD_NOT_EMPTY_ID)
            )
        },
        assertError = { error ->
            assertEquals("validation-id-empty", error.code)
            assertEquals("id", error.field)
        }
    )

internal fun testCardLockIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId(GOOD_NOT_EMPTY_ID),
                lock = FcCardLock.NONE
            )
        },
        assertError = { error ->
            assertEquals("validation-lock-empty", error.code)
            assertEquals("lock", error.field)
        }
    )

internal fun testCardIdMatchesFormatValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId(BAD_NOT_EMPTY_ID),
                lock = FcCardLock(GOOD_NOT_EMPTY_ID)
            )
        },
        assertError = { error ->
            assertEquals("validation-id-badFormat", error.code)
            assertEquals("id", error.field)
        }
    )

internal fun testCardLockMatchesFormatValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        configureContext = {
            requestCard = Card(
                id = CardId(GOOD_NOT_EMPTY_ID),
                lock = FcCardLock(BAD_NOT_EMPTY_ID)
            )
        },
        assertError = { error ->
            assertEquals("validation-lock-badFormat", error.code)
            assertEquals("lock", error.field)
        }
    )



