package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import kotlin.test.assertEquals

internal fun testCardIdIsEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        requestCard = Card(
            id = CardId(GOOD_NOT_EMPTY_ID),
            front = GOOD_FRONT,
            back = GOOD_BACK
        )
    ) { error ->
        assertEquals("validation-id-not-empty", error.code)
        assertEquals("id", error.field)
    }

internal fun testFrontIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        requestCard = Card(
            id = CardId.NONE,
            front = SOME_EMPTY_STRING,
            back = GOOD_BACK
        )
    ) { error ->
        assertEquals("validation-front-empty", error.code)
        assertEquals("front", error.field)
    }

internal fun testBackIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        requestCard = Card(
            id = CardId.NONE,
            front = GOOD_FRONT,
            back = SOME_EMPTY_STRING
        )
    ) { error ->
        assertEquals("validation-back-empty", error.code)
        assertEquals("back", error.field)
    }

internal fun testCardIdIsNotEmptyValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        requestCard = Card(id = CardId.NONE)
    ) { error ->
        assertEquals("validation-id-empty", error.code)
        assertEquals("id", error.field)
    }

internal fun testCardIdHasProperFormatValidation(processor: FcCardProcessor, command: CardCommand) =
    runSingleErrorValidationTest(
        processor = processor,
        command = command,
        requestCard = Card(id = CardId(BAD_NOT_EMPTY_ID))
    ) { error ->
        assertEquals("validation-id-badFormat", error.code)
        assertEquals("id", error.field)
    }



