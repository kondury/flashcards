package com.github.kondury.flashcards.cards.common

import kotlinx.datetime.Instant
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.stubs.FcStub

data class CardContext(
    var command: CardCommand = CardCommand.NONE,
    var state: FcState = FcState.NONE,
    var errors: MutableList<FcError> = mutableListOf(),

    var workMode: FcWorkMode = FcWorkMode.PROD,
    var stubCase: FcStub = FcStub.NONE,

    var requestId: FcRequestId = FcRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var cardRequest: Card = Card(),
    var cardResponse: Card = Card(),
)

