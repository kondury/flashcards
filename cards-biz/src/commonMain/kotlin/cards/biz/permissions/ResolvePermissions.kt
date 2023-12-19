package com.github.kondury.flashcards.cards.biz.permissions

import com.github.kondury.flashcards.cards.auth.resolveUserPermissions
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker


fun CorChainDsl<CardContext>.resolvePermissions() = worker {
    this.title = "Calculate all permissions according to the groups where principal is included"
    activeIf { state == FcState.RUNNING }
    handle {
        userPermissions.addAll(resolveUserPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $userPermissions")
    }
}
