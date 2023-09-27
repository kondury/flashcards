package com.github.kondury.flashcards.common

import kotlinx.datetime.Instant
import com.github.kondury.flashcards.common.models.*
import com.github.kondury.flashcards.common.stubs.FcStubs

data class PlacedCardContext(
    var command: PlacedCardCommand = PlacedCardCommand.NONE,
    var state: FcState = FcState.NONE,
    var errors: MutableList<FcError> = mutableListOf(),

    var workMode: FcRunMode = FcRunMode.PROD,
    var stubCase: FcStubs = FcStubs.NONE,

    var requestId: FcRequestId = FcRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    // before-known placed card data (if exists) for create, delete and move commands
    var placedCardRequest: PlacedCard = PlacedCard(),
    // after request handling placed card data for create, move and select commands
    var placedCardResponse: PlacedCard = PlacedCard(),
    // init/select commands
    var ownerId: UserId = UserId.NONE,
    // init/select commands
    var workBox: Box = Box.NONE,
    // box after for move command
    var boxAfter: Box = Box.NONE,
    // sorting order for select command
    var searchStrategy: SearchStrategy = SearchStrategy.NONE,
)

