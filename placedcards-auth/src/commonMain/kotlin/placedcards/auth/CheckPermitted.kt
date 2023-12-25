package com.github.kondury.flashcards.placedcards.auth

import com.github.kondury.flashcards.placedcards.common.permissions.FcPrincipalRelations
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserPermissions
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand


fun checkPermitted(
    command: PlacedCardCommand,
    relations: Iterable<FcPrincipalRelations>,
    permissions: Iterable<FcUserPermissions>,
) = relations.asSequence()
    .flatMap { relation ->
        permissions.map { permission ->
            AccessCondition(command, permission, relation)
        }
    }.any { it in accessConditions }

private data class AccessCondition(
    val command: PlacedCardCommand,
    val permission: FcUserPermissions,
    val relation: FcPrincipalRelations
)

private val accessConditions = setOf(
    // Create
    AccessCondition(
        command = PlacedCardCommand.CREATE_PLACED_CARD,
        permission = FcUserPermissions.CREATE_PLACED_CARD_ANY,
        relation = FcPrincipalRelations.ANY,
    ),
    AccessCondition(
        command = PlacedCardCommand.CREATE_PLACED_CARD,
        permission = FcUserPermissions.CREATE_PLACED_CARD_OWN,
        relation = FcPrincipalRelations.OWN,
    ),

    // Delete
    AccessCondition(
        command = PlacedCardCommand.DELETE_PLACED_CARD,
        permission = FcUserPermissions.DELETE_PLACED_CARD_ANY,
        relation = FcPrincipalRelations.ANY,
    ),
    AccessCondition(
        command = PlacedCardCommand.DELETE_PLACED_CARD,
        permission = FcUserPermissions.DELETE_PLACED_CARD_OWN,
        relation = FcPrincipalRelations.OWN,
    ),

    // Move
    AccessCondition(
        command = PlacedCardCommand.MOVE_PLACED_CARD,
        permission = FcUserPermissions.MOVE_PLACED_CARD_ANY,
        relation = FcPrincipalRelations.ANY,
    ),
    AccessCondition(
        command = PlacedCardCommand.MOVE_PLACED_CARD,
        permission = FcUserPermissions.MOVE_PLACED_CARD_OWN,
        relation = FcPrincipalRelations.OWN,
    ),

    // Select
    AccessCondition(
        command = PlacedCardCommand.SELECT_PLACED_CARD,
        permission = FcUserPermissions.SELECT_PLACED_CARD_ANY,
        relation = FcPrincipalRelations.ANY,
    ),
    AccessCondition(
        command = PlacedCardCommand.SELECT_PLACED_CARD,
        permission = FcUserPermissions.SELECT_PLACED_CARD_OWN,
        relation = FcPrincipalRelations.OWN,
    ),

    // Init
    AccessCondition(
        command = PlacedCardCommand.INIT_PLACED_CARD,
        permission = FcUserPermissions.INIT_PLACED_CARD_ANY,
        relation = FcPrincipalRelations.ANY,
    ),
    AccessCondition(
        command = PlacedCardCommand.INIT_PLACED_CARD,
        permission = FcUserPermissions.INIT_PLACED_CARD_OWN,
        relation = FcPrincipalRelations.OWN,
    ),
)