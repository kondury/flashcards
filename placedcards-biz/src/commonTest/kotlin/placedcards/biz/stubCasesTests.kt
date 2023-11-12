package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub
import kotlin.test.assertEquals

// create - placedCard.cardId
internal fun testWrongCardIdErrorStub(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.WRONG_CARD_ID,
    ) { error ->
        with(error) {
            assertEquals("validation-stub", group)
            assertEquals("validation-${command.name.lowercase()}-cardId-", code)
            assertEquals("cardId", field)
        }
    }

// create - placedCard.ownerId
// init - ownerId
// select - ownerId
internal fun testWrongOwnerIdErrorStub(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.WRONG_OWNER_ID,
    ) { error ->
        with(error) {
            assertEquals("validation-stub", group)
            assertEquals("validation-${command.name.lowercase()}-ownerId-", code)
            assertEquals("ownerId", field)
        }
    }

// create - placeCard.box
// init, select - workBox
// move - boxAfter
internal fun testWrongBoxErrorStub(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.WRONG_BOX,
    ) { error ->
        with(error) {
            assertEquals("validation-stub", group)
            assertEquals("validation-${command.name.lowercase()}-box-", code)
            assertEquals("box", field)
        }
    }

internal fun testWrongPlacedCardIdErrorStub(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.WRONG_PLACED_CARD_ID,
    ) { error ->
        with(error) {
            assertEquals("validation-stub", group)
            assertEquals("validation-${command.name.lowercase()}-placedCardId-", code)
            assertEquals("placedCardId", field)
        }
    }

internal fun testWrongSearchStrategyErrorStub(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.WRONG_SEARCH_STRATEGY,
    ) { error ->
        with(error) {
            assertEquals("validation-stub", group)
            assertEquals("validation-${command.name.lowercase()}-searchStrategy-", code)
            assertEquals("searchStrategy", field)
        }
    }

internal fun testNotFoundStubError(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.NOT_FOUND,
    ) { error ->
        with(error) {
            assertEquals("db-error-stub", group)
            assertEquals("db-error-stub-not-found", code)
            assertEquals("", field)
        }
    }

internal fun testDatabaseErrorStub(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.DB_ERROR,
    ) { error ->
        with(error) {
            assertEquals("db-error-stub", group)
            assertEquals("db-error-general", code)
            assertEquals("", field)
        }
    }

internal fun testNoCaseStubError(processor: FcPlacedCardProcessor, command: PlacedCardCommand) =
    runErrorStubTest(
        processor = processor,
        command = command,
        stub = FcStub.NONE,
    ) { error ->
        with(error) {
            assertEquals("error-stub", group)
            assertEquals("unsupported-case-stub", code)
            assertEquals("stub", field)
        }
    }

