package com.github.kondury.flashcards.placedcards.common

import kotlinx.datetime.Instant
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub

data class PlacedCardContext(
    var command: PlacedCardCommand = PlacedCardCommand.NONE,
    var state: FcState = FcState.NONE,
    var errors: MutableList<FcError> = mutableListOf(),

    var workMode: FcWorkMode = FcWorkMode.PROD,
    var stubCase: FcStub = FcStub.NONE,

    var requestId: FcRequestId = FcRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    // create command
    var requestPlacedCard: PlacedCard = PlacedCard.EMPTY,
    var validatingPlacedCard: PlacedCard = PlacedCard.EMPTY,
    var validatedPlacedCard: PlacedCard = PlacedCard.EMPTY,

    // delete/move commands
    var requestPlacedCardId: PlacedCardId = PlacedCardId.NONE,
    var validatingPlacedCardId: PlacedCardId = PlacedCardId.NONE,
    var validatedPlacedCardId: PlacedCardId = PlacedCardId.NONE,

    // init/select commands
    var requestOwnerId: UserId = UserId.NONE,
    var validatingOwnerId: UserId = UserId.NONE,
    var validatedOwnerId: UserId = UserId.NONE,

    // init/select commands
    var requestWorkBox: FcBox = FcBox.NONE,
    var validatingWorkBox: FcBox = FcBox.NONE,
    var validatedWorkBox: FcBox = FcBox.NONE,

    // box after for move command
    var requestBoxAfter: FcBox = FcBox.NONE,
    var validatingBoxAfter: FcBox = FcBox.NONE,
    var validatedBoxAfter: FcBox = FcBox.NONE,

    // sorting order for select command
    var requestSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,
    var validatingSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,
    var validatedSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,

    // after request handling placed card data for create, move and select commands
    var responsePlacedCard: PlacedCard = PlacedCard.EMPTY,
)

