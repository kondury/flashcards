package com.github.kondury.flashcards.cards.auth

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.permissions.FcPrincipalRelations


fun resolveRelationsTo(card: Card): Set<FcPrincipalRelations> = setOfNotNull(
    FcPrincipalRelations.ANY,
    FcPrincipalRelations.NEW.takeIf { card.id == CardId.NONE },
)
