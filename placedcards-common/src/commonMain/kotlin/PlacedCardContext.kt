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

    // before-known placed card data (if exists) for create, delete and move commands
    var requestPlacedCard: PlacedCard = PlacedCard.EMPTY,
    // init/select commands
    var requestOwnerId: UserId = UserId.NONE,
    // init/select commands
    var requestWorkBox: FcBox = FcBox.NONE,
    // box after for move command
    var requestBoxAfter: FcBox = FcBox.NONE,
    // sorting order for select command
    var requestSearchStrategy: FcSearchStrategy = FcSearchStrategy.NONE,

    // after request handling placed card data for create, move and select commands
    var responsePlacedCard: PlacedCard = PlacedCard.EMPTY,
)

