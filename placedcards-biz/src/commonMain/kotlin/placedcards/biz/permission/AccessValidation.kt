package com.github.kondury.flashcards.placedcards.biz.permission

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.auth.checkPermitted
import com.github.kondury.flashcards.placedcards.auth.resolveRelations
import com.github.kondury.flashcards.placedcards.biz.fail
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcError
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*
import com.github.kondury.flashcards.placedcards.common.models.UserId


fun CorChainDsl<PlacedCardContext>.accessValidation() = chain {
    this.title = "Check access permissions via principal group and access permissions table"
    activeIf { state == FcState.RUNNING }
    worker("Calculate placed card relation to the principal") {

        println()
        println("repoReadPlacedCard: $repoReadPlacedCard")

        val ownerId = when (command) {
            CREATE_PLACED_CARD -> validatedPlacedCard.ownerId
            MOVE_PLACED_CARD, DELETE_PLACED_CARD -> repoReadPlacedCard.ownerId
            SELECT_PLACED_CARD, INIT_PLACED_CARD -> validatedOwnerId
            NONE -> UserId.NONE
        }
        principalRelations = resolveRelations(ownerId, principal.id)
//        println("RELATIONS: $principalRelations")
    }
    checkPermissions()
    handleNotPermitted()
}

fun CorChainDsl<PlacedCardContext>.checkPermissions() =
    worker("Calculate the summary placed card command permission") {
        isPermitted = checkPermitted(command, principalRelations, userPermissions)
    }

fun CorChainDsl<PlacedCardContext>.handleNotPermitted() = worker {
    this.title = "Handle not permitted command"
    activeIf { !isPermitted }
    handle { fail(FcError(message = "User is not allowed to perform this operation")) }
}


