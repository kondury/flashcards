package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.DebugResource
import com.github.kondury.flashcards.cards.api.v1.models.DebugStub
import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.api.v1.models.RunMode
import com.github.kondury.flashcards.cards.common.models.FcRequestId
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cards.common.stubs.FcStub

internal fun IRequest?.requestId() = this?.requestId?.let { FcRequestId(it) } ?: FcRequestId.NONE

internal fun DebugResource?.transportToWorkMode(): FcWorkMode = when (this?.mode) {
    RunMode.PROD -> FcWorkMode.PROD
    RunMode.TEST -> FcWorkMode.TEST
    RunMode.STUB -> FcWorkMode.STUB
    null -> FcWorkMode.PROD
}

internal fun DebugResource?.transportToStubCase(): FcStub = when (this?.stub) {
    DebugStub.SUCCESS -> FcStub.SUCCESS
    DebugStub.NOT_FOUND -> FcStub.NOT_FOUND
    DebugStub.WRONG_CARD_ID -> FcStub.WRONG_CARD_ID
    DebugStub.WRONG_FRONT_SIDE -> FcStub.WRONG_FRONT_SIDE
    DebugStub.WRONG_BACK_SIDE -> FcStub.WRONG_BACK_SIDE
    DebugStub.CANNOT_DELETE -> FcStub.CANNOT_DELETE
    DebugStub.UNKNOWN_ERROR -> FcStub.UNKNOWN_ERROR;
    null -> FcStub.NONE
}