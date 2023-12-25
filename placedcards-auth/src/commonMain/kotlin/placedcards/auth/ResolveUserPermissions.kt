package com.github.kondury.flashcards.placedcards.auth

import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserPermissions

fun resolveUserPermissions(groups: Iterable<FcUserGroups>): Set<FcUserPermissions> {
    val allowed = groups.mapNotNull { allowedGroupPermissions[it] }.flatten().toSet()
    val denied = groups.mapNotNull { deniedGroupPermissions[it] }.flatten().toSet()
    return allowed - denied
}

private val allowedGroupPermissions = mapOf(
    FcUserGroups.USER to setOf(
        FcUserPermissions.CREATE_PLACED_CARD_OWN,
        FcUserPermissions.DELETE_PLACED_CARD_OWN,
        FcUserPermissions.MOVE_PLACED_CARD_OWN,
        FcUserPermissions.SELECT_PLACED_CARD_OWN,
        FcUserPermissions.INIT_PLACED_CARD_OWN,
    ),
    FcUserGroups.ADMIN to setOf(
        FcUserPermissions.CREATE_PLACED_CARD_ANY,
        FcUserPermissions.DELETE_PLACED_CARD_ANY,
        FcUserPermissions.MOVE_PLACED_CARD_ANY,
        FcUserPermissions.SELECT_PLACED_CARD_ANY,
        FcUserPermissions.INIT_PLACED_CARD_ANY,
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
