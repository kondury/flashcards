package com.github.kondury.flashcards.placedcards.common

import kotlinx.datetime.Instant
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository


data class PlacedCardContext(
    var command: PlacedCardCommand = PlacedCardCommand.NONE,
    var state: FcState = FcState.NONE,
    var errors: MutableList<FcError> = mutableListOf(),

    var repositoryConfig: PlacedCardRepositoryConfig = PlacedCardRepositoryConfig.NONE,

    var workMode: FcWorkMode = FcWorkMode.PROD,
    var stubCase: FcStub = FcStub.NONE,

    var requestId: FcRequestId = FcRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    // create command:  ownerId, box, cardId, createdAt, updatedAt
    // move command: id, box, updatedAt
    // delete command: id
    var requestPlacedCard: PlacedCard = PlacedCard.EMPTY,
    var validatingPlacedCard: PlacedCard = PlacedCard.EMPTY,
    var validatedPlacedCard: PlacedCard = PlacedCard.EMPTY,

    // init/select commands
    var requestOwnerId: UserId = UserId.NONE,
    var validatingOwnerId: UserId = UserId.NONE,
    var validatedOwnerId: UserId = UserId.NONE,

    // init/select commands
    var requestWorkBox: FcBox = FcBox.NONE,
    var validatingWorkBox: FcBox = FcBox.NONE,
    var validatedWorkBox: FcBox = FcBox.NONE,

    // select command: sorting order
    var requestSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,
    var validatingSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,
    var validatedSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,

    // after request handling placed card data for create, move and select commands
    var responsePlacedCard: PlacedCard = PlacedCard.EMPTY,

    var repository: PlacedCardRepository = PlacedCardRepository.NoOpPlacedCardRepository,
    var repoReadPlacedCard: PlacedCard = PlacedCard.EMPTY,
    var repoPreparedPlacedCard: PlacedCard = PlacedCard.EMPTY,
    var repoResponsePlacedCard: PlacedCard = PlacedCard.EMPTY
)

