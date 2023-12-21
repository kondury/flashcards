package com.github.kondury.flashcards.placedcards.biz.common

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.common.permissions.FcPrincipalModel
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups


fun PlacedCardContext.setAdminPrincipal(userId: UserId = UserId("321")) =
    setPrincipal(userId, setOf(FcUserGroups.ADMIN))

fun PlacedCardContext.setPrincipal(userId: UserId = UserId("321"), groups: Set<FcUserGroups>) {
    principal = FcPrincipalModel(
        id = userId,
        groups = setOf(FcUserGroups.TEST) + groups
    )
}
