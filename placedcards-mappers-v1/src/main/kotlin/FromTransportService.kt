package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.DebugResource
import com.github.kondury.flashcards.placedcards.api.v1.models.DebugStub
import com.github.kondury.flashcards.placedcards.api.v1.models.IRequest
import com.github.kondury.flashcards.placedcards.api.v1.models.RunMode
import com.github.kondury.flashcards.placedcards.common.models.FcRequestId
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub

internal fun IRequest?.requestIdOrNone() = this?.requestId?.let { FcRequestId(it) } ?: FcRequestId.NONE

internal fun DebugResource?.toWorkMode(): FcWorkMode = when (this?.mode) {
    RunMode.PROD -> FcWorkMode.PROD
    RunMode.TEST -> FcWorkMode.TEST
    RunMode.STUB -> FcWorkMode.STUB
    null -> FcWorkMode.PROD
}

internal fun DebugResource?.toStubCaseOrNone(): FcStub = when (this?.stub) {
    DebugStub.SUCCESS -> FcStub.SUCCESS
    DebugStub.NOT_FOUND -> FcStub.NOT_FOUND
    DebugStub.WRONG_CARD_ID -> FcStub.WRONG_CARD_ID
    DebugStub.WRONG_PLACED_CARD_ID -> FcStub.WRONG_PLACED_CARD_ID
    DebugStub.WRONG_OWNER_ID -> FcStub.WRONG_OWNER_ID
    DebugStub.WRONG_BOX -> FcStub.WRONG_BOX
    DebugStub.WRONG_SEARCH_STRATEGY -> FcStub.WRONG_SEARCH_STRATEGY
    null -> FcStub.NONE
}