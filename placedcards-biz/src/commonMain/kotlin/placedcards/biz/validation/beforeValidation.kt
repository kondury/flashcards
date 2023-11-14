package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*

internal fun CorChainDsl<PlacedCardContext>.beforeCreatePlacedCardValidation() =
    beforeValidation(CREATE_PLACED_CARD) {
        validatingPlacedCard = requestPlacedCard.normalized
    }

internal fun CorChainDsl<PlacedCardContext>.beforeMovePlacedCardValidation() =
    beforeValidation(MOVE_PLACED_CARD) {
        validatingPlacedCardId = requestPlacedCardId.normalized
        validatingBoxAfter = requestBoxAfter
    }

internal fun CorChainDsl<PlacedCardContext>.beforeSelectPlacedCardValidation() =
    beforeValidation(SELECT_PLACED_CARD) {
        validatingOwnerId = requestOwnerId.normalized
        validatingWorkBox = requestWorkBox
        validatingSearchStrategy = requestSearchStrategy
    }

internal fun CorChainDsl<PlacedCardContext>.beforeDeletePlacedCardValidation() =
    beforeValidation(DELETE_PLACED_CARD) {
        validatingPlacedCardId = requestPlacedCardId.normalized
    }

internal fun CorChainDsl<PlacedCardContext>.beforeInitPlacedCardValidation() =
    beforeValidation(INIT_PLACED_CARD) {
        validatingOwnerId = requestOwnerId.normalized
        validatingWorkBox = requestWorkBox
    }

internal fun CorChainDsl<PlacedCardContext>.beforeValidation(
    command: PlacedCardCommand,
    handler: suspend PlacedCardContext.() -> Unit
) = worker {
    this.title = "Prepare context before validation of ${command.name} params"
    activeIf { state == FcState.RUNNING }
    handle { handler() }
}

private val CardId.normalized: CardId
    get() = CardId(asString().normalized)

private val UserId.normalized: UserId
    get() = UserId(asString().normalized)

private val PlacedCardId.normalized: PlacedCardId
    get() = PlacedCardId(asString().normalized)

private val PlacedCard.normalized: PlacedCard
    get() = this.copy(
        id = id.normalized,
        ownerId = ownerId.normalized,
        cardId = cardId.normalized,
    )

private val String.normalized: String
    get() = trim()
