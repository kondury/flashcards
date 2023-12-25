package com.github.kondury.flashcards.cards.auth

import com.github.kondury.flashcards.cards.common.permissions.FcUserGroups
import com.github.kondury.flashcards.cards.common.permissions.FcUserPermissions

fun resolveUserPermissions(groups: Iterable<FcUserGroups>): Set<FcUserPermissions> {
    val allowed = groups.mapNotNull { allowedGroupPermissions[it] }.flatten().toSet()
    val denied = groups.mapNotNull { deniedGroupPermissions[it] }.flatten().toSet()
    return allowed - denied
}

private val allowedGroupPermissions = mapOf(
    FcUserGroups.USER to setOf(
        FcUserPermissions.READ_CARD,
    ),
    FcUserGroups.ADMIN to setOf(
        FcUserPermissions.CREATE_CARD,
        FcUserPermissions.READ_CARD,
        FcUserPermissions.DELETE_CARD,
    ),
    FcUserGroups.TEST to setOf(),
    FcUserGroups.BANNED to setOf(),
)

private val deniedGroupPermissions = mapOf(
    FcUserGroups.USER to setOf(),
    FcUserGroups.ADMIN to setOf(),
    FcUserGroups.TEST to setOf(),
    FcUserGroups.BANNED to FcUserPermissions.entries.toSet()
)
