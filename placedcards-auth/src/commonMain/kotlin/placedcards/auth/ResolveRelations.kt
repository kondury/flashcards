package com.github.kondury.flashcards.placedcards.auth

import com.github.kondury.flashcards.placedcards.common.permissions.FcPrincipalRelations
import com.github.kondury.flashcards.placedcards.common.models.UserId


fun resolveRelations(ownerId: UserId, principalId: UserId): Set<FcPrincipalRelations> = setOfNotNull(
    FcPrincipalRelations.ANY,
    FcPrincipalRelations.OWN.takeIf { principalId == ownerId },
)

