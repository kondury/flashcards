package com.github.kondury.flashcards.cards.biz.permissions

import com.github.kondury.flashcards.cards.auth.checkPermitted
import com.github.kondury.flashcards.cards.auth.resolveRelationsTo
import com.github.kondury.flashcards.cards.biz.fail
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.cor.dsl.worker


fun CorChainDsl<CardContext>.accessValidation() = chain {
    this.title = "Check access permissions via principal group and access permissions table"
    activeIf { state == FcState.RUNNING }
    worker("Calculate card relation to the principal") {
        principalRelations = resolveRelationsTo(repoReadCard)
    }
    worker("Calculate the summary card command permission") {
        isPermitted = checkPermitted(command, principalRelations, userPermissions)
    }
    worker {
        this.title = "Handle not permitted command"
        activeIf { !isPermitted }
        handle {
            fail(FcError(message = "User is not allowed to perform this operation"))
        }
    }
}
