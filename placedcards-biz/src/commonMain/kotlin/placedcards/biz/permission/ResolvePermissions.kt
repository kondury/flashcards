package com.github.kondury.flashcards.placedcards.biz.permission

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.auth.resolveUserPermissions
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState


fun CorChainDsl<PlacedCardContext>.resolvePermissions() = worker {
    this.title = "Calculate all permissions according to the groups where principal is included"
    activeIf { state == FcState.RUNNING }
    handle {
        userPermissions.addAll(resolveUserPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $userPermissions")
    }
}
