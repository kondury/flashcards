package com.github.kondury.flashcards.cards.biz.validation

import com.github.kondury.flashcards.cards.biz.fail
import com.github.kondury.flashcards.cards.biz.validationError
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcError.Level
import com.github.kondury.flashcards.cards.common.models.isEmpty
import com.github.kondury.flashcards.cards.common.models.isNotEmpty
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

private const val ID_FORMAT_PATTERN = "^[0-9a-zA-Z-]+$"

internal fun CorChainDsl<CardContext>.validateFrontIsNotEmpty() = worker {
    this.title = "Validating: front is not empty"
    activeIf { validatingCard.front.isEmpty() }
    handle {
        fail(
            validationError(
                field = "front",
                violationCode = "empty",
                description = "field must not be empty",
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<CardContext>.validateBackIsNotEmpty() = worker {
    this.title = "Validating: back is not empty"
    activeIf { validatingCard.back.isEmpty() }
    handle {
        fail(
            validationError(
                field = "back",
                violationCode = "empty",
                description = "field must not be empty",
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<CardContext>.validateCardIdIsNotEmpty() = worker {
    this.title = "Validating: card id is not empty"
    activeIf { validatingCard.id.isEmpty() }
    handle {
        fail(
            validationError(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty",
                level = Level.INFO
            )
        )
    }
}

internal fun CorChainDsl<CardContext>.validateCardIdIsEmpty() = worker {
    this.title = "Validating: card id is empty"
    activeIf { validatingCard.id.isNotEmpty() }
    handle {
        fail(
            validationError(
                field = "id",
                violationCode = "not-empty",
                description = "must be empty",
                level = Level.INFO
            )
        )
    }
}

fun CorChainDsl<CardContext>.validateCardIdMatchesFormat() = worker {
    this.title = "Validating: card id has proper format"
    activeIf { validatingCard.id.isNotEmpty() && !validatingCard.id.asString().matches(Regex(ID_FORMAT_PATTERN)) }
    handle {
        val encodedId = validatingCard.id.asString()
//            .replace("<", "&lt;")
//            .replace(">", "&gt;")
        fail(
            validationError(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters, numbers and hyphens",
                level = Level.INFO
            )
        )
    }
}


