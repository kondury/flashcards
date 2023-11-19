package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.biz.validationError
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.fail
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.FcError.Level

private const val ID_FORMAT_PATTERN = "^[0-9a-zA-Z-]+$"
private const val MSG_MISMATCHING_FORMAT = "mismatching format"
private const val MSG_FIELD_IS_REQUIRED = "field is required"
private const val MSG_FIELD_SHOULD_BE_EMPTY = "field should be empty"

private const val CODE_EMPTY = "empty"
private const val CODE_NOT_EMPTY = "not-empty"
private const val CODE_MISMATCHING_FORMAT = "badFormat"

private const val FIELD_SEARCH_STRATEGY = "searchStrategy"
private const val FIELD_OWNER_ID = "ownerId"
private const val FIELD_CARD_ID = "cardId"
private const val FIELD_PLACED_CARD_ID = "placedCardId"
private const val FIELD_BOX = "box"


internal fun CorChainDsl<PlacedCardContext>.validateSearchStrategyIsNotEmpty(
    command: PlacedCardCommand,
    getValue: (PlacedCardContext).() -> FcSearchStrategy
) = worker {
    title = "Validating: search strategy is not empty"
    activeIf { getValue().isEmpty() }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_SEARCH_STRATEGY,
                violationCode = CODE_EMPTY,
                description = MSG_FIELD_IS_REQUIRED,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validatePlacedCardIdMatchesFormat(
    command: PlacedCardCommand,
    getValue: (PlacedCardContext).() -> PlacedCardId
) = worker {
    title = "Validating: placed card id has proper format"
    activeIf { getValue().isNotEmpty() && !getValue().asString().matches(Regex(ID_FORMAT_PATTERN)) }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_PLACED_CARD_ID,
                violationCode = CODE_MISMATCHING_FORMAT,
                description = MSG_MISMATCHING_FORMAT,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validatePlacedCardIdIsNotEmpty(
    command: PlacedCardCommand,
    getValue: (PlacedCardContext).() -> PlacedCardId
) = worker {
    title = "Validating: placed card id is not empty"
    activeIf { getValue().isEmpty() }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_PLACED_CARD_ID,
                violationCode = CODE_EMPTY,
                description = MSG_FIELD_IS_REQUIRED,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validateOwnerIdMatchesFormat(
    command: PlacedCardCommand,
    getValue: (PlacedCardContext).() -> UserId
) = worker {
    title = "Validating: owner id has proper format"
    activeIf { getValue().isNotEmpty() && !getValue().asString().matches(Regex(ID_FORMAT_PATTERN)) }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_OWNER_ID,
                violationCode = CODE_MISMATCHING_FORMAT,
                description = MSG_MISMATCHING_FORMAT,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validateOwnerIdIsNotEmpty(
    command: PlacedCardCommand,
    getValue: (PlacedCardContext).() -> UserId
) = worker {
    title = "Validating: owner id is not empty"
    activeIf { getValue().isEmpty() }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_OWNER_ID,
                violationCode = CODE_EMPTY,
                description = MSG_FIELD_IS_REQUIRED,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validatePlacedCardIdIsEmpty(
    command: PlacedCardCommand,
    getValue: (PlacedCardContext).() -> PlacedCardId
) = worker {
    title = "Validating: placed card id is empty"
    activeIf { getValue().isNotEmpty() }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_PLACED_CARD_ID,
                violationCode = CODE_NOT_EMPTY,
                description = MSG_FIELD_SHOULD_BE_EMPTY,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validateCardIdIsNotEmpty(
    command: PlacedCardCommand,
    getValue: PlacedCardContext.() -> CardId
) = worker {
    title = "Validating: card id is not empty"
    activeIf { getValue().isEmpty() }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_CARD_ID,
                violationCode = CODE_EMPTY,
                description = MSG_FIELD_IS_REQUIRED,
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<PlacedCardContext>.validateCardIdMatchesFormat(
    command: PlacedCardCommand,
    getValue: PlacedCardContext.() -> CardId
) = worker {
    title = "Validating: card id matches format"
    activeIf { getValue().isNotEmpty() && !getValue().asString().matches(Regex(ID_FORMAT_PATTERN)) }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_CARD_ID,
                violationCode = CODE_MISMATCHING_FORMAT,
                description = MSG_MISMATCHING_FORMAT,
                level = Level.INFO
            )
        )
    }

}

internal fun CorChainDsl<PlacedCardContext>.validateBoxIsNotEmpty(
    command: PlacedCardCommand,
    getValue: PlacedCardContext.() -> FcBox
) = worker {
    title = "Validating: box is not empty"
    activeIf { getValue().isEmpty() }
    handle {
        fail(
            validationError(
                command = command,
                field = FIELD_BOX,
                violationCode = CODE_EMPTY,
                description = MSG_FIELD_IS_REQUIRED,
                level = Level.INFO
            )
        )
    }
}
