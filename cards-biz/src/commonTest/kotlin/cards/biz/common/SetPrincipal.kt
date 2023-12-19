package com.github.kondury.flashcards.cards.biz.common

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.UserId
import com.github.kondury.flashcards.cards.common.permissions.FcPrincipalModel
import com.github.kondury.flashcards.cards.common.permissions.FcUserGroups


fun CardContext.setAdminPrincipal(userId: UserId = UserId("321")) = setPrincipal(userId, setOf(FcUserGroups.ADMIN))

fun CardContext.setPrincipal(userId: UserId = UserId("321"), groups: Set<FcUserGroups>) {
    principal = FcPrincipalModel(
        id = userId,
        groups = setOf(FcUserGroups.TEST) + groups
    )
}
