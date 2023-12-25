package com.github.kondury.flashcards.cards.common

import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.permissions.FcPrincipalModel
import com.github.kondury.flashcards.cards.common.permissions.FcPrincipalRelations
import com.github.kondury.flashcards.cards.common.permissions.FcUserPermissions
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.common.stubs.FcStub
import kotlinx.datetime.Instant

data class CardContext(
    var command: CardCommand = CardCommand.NONE,
    var state: FcState = FcState.NONE,
    var errors: MutableList<FcError> = mutableListOf(),

    var repositoryConfig: CardRepositoryConfig = CardRepositoryConfig.NONE,

    var workMode: FcWorkMode = FcWorkMode.PROD,
    var stubCase: FcStub = FcStub.NONE,

    var requestId: FcRequestId = FcRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var requestCard: Card = Card.EMPTY,
    var validatingCard: Card = Card.EMPTY,
    var validatedCard: Card = Card.EMPTY,
    var responseCard: Card = Card.EMPTY,

    var repository: CardRepository = CardRepository.NoOpCardRepository,
    var repoReadCard: Card = Card.EMPTY,
    var repoPreparedCard: Card = Card.EMPTY,
    var repoResponseCard: Card = Card.EMPTY,

    var principal: FcPrincipalModel = FcPrincipalModel.NONE,
    val userPermissions: MutableSet<FcUserPermissions> = mutableSetOf(),
    var principalRelations: Set<FcPrincipalRelations> = setOf(FcPrincipalRelations.ANY),
    var isPermitted: Boolean = false,
)

