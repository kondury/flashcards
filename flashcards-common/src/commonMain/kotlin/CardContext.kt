package com.github.kondury.flashcards.common

import kotlinx.datetime.Instant
import com.github.kondury.flashcards.common.models.*
import com.github.kondury.flashcards.common.stubs.FcStubs

data class CardContext(
    var command: CardCommand = CardCommand.NONE,
    var state: FcState = FcState.NONE,
    var errors: MutableList<FcError> = mutableListOf(),

    var workMode: FcRunMode = FcRunMode.PROD,
    var stubCase: FcStubs = FcStubs.NONE,

    var requestId: FcRequestId = FcRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    // todo define sensitive for context data
    var cardRequest: Card = Card(),
    var cardResponse: Card = Card(),
)

