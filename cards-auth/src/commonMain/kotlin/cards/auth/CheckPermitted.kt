package com.github.kondury.flashcards.cards.auth

import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.permissions.FcPrincipalRelations
import com.github.kondury.flashcards.cards.common.permissions.FcUserPermissions


fun checkPermitted(
    command: CardCommand,
    relations: Iterable<FcPrincipalRelations>,
    permissions: Iterable<FcUserPermissions>,
) = relations.asSequence().flatMap { relation ->
    permissions.map { permission ->
        AccessCondition(
            command = command,
            permission = permission,
            relation = relation,
        )
    }
}.any { it in accessConditions }

private data class AccessCondition(
    val command: CardCommand, val permission: FcUserPermissions, val relation: FcPrincipalRelations
)

private val accessConditions = setOf(
    // Create
    AccessCondition(
        command = CardCommand.CREATE_CARD,
        permission = FcUserPermissions.CREATE_CARD,
        relation = FcPrincipalRelations.NEW,
    ),

    // Read
    AccessCondition(
        command = CardCommand.READ_CARD,
        permission = FcUserPermissions.READ_CARD,
        relation = FcPrincipalRelations.ANY,
    ),

    // Delete
    AccessCondition(
        command = CardCommand.DELETE_CARD,
        permission = FcUserPermissions.DELETE_CARD,
        relation = FcPrincipalRelations.ANY,
    ),
)