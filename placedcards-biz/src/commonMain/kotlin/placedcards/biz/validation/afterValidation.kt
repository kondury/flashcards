package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand


internal fun CorChainDsl<PlacedCardContext>.afterCreatePlacedCardValidation() =
    afterValidation(PlacedCardCommand.CREATE_PLACED_CARD) {
        validatedPlacedCard = validatingPlacedCard
    }

internal fun CorChainDsl<PlacedCardContext>.afterMovePlacedCardValidation() =
    afterValidation(PlacedCardCommand.MOVE_PLACED_CARD) {
        validatedPlacedCardId =  validatingPlacedCardId
        validatedBoxAfter = validatingBoxAfter
    }


internal fun CorChainDsl<PlacedCardContext>.afterSelectPlacedCardValidation() =
    afterValidation(PlacedCardCommand.SELECT_PLACED_CARD) {
        validatedOwnerId = validatingOwnerId
        validatedWorkBox = validatingWorkBox
        validatedSearchStrategy = validatingSearchStrategy
    }

internal fun CorChainDsl<PlacedCardContext>.afterDeletePlacedCardValidation() =
    afterValidation(PlacedCardCommand.DELETE_PLACED_CARD) {
        validatedPlacedCardId = validatingPlacedCardId
    }

internal fun CorChainDsl<PlacedCardContext>.afterInitPlacedCardValidation() =
    afterValidation(PlacedCardCommand.INIT_PLACED_CARD) {
        validatedOwnerId = validatingOwnerId
        validatedWorkBox = validatingWorkBox
    }

fun CorChainDsl<PlacedCardContext>.afterValidation(
    command: PlacedCardCommand,
    handler: suspend PlacedCardContext.() -> Unit
) =
    worker {
        this.title = "Prepare context after successful validation of ${command.name} params"
        activeIf { state == FcState.RUNNING }
        handle { handler() }
    }


