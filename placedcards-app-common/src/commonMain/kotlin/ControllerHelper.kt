package com.github.kondury.flashcards.placedcards.app.common

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.asFcError
import com.github.kondury.flashcards.placedcards.common.models.FcState
import kotlinx.datetime.Clock


suspend inline fun FcPlacedCardProcessor.process(
    crossinline receiveThenFromTransport: suspend (PlacedCardContext) -> Unit,
    crossinline toTransportThenRespond: suspend (PlacedCardContext) -> Unit,
) {
    PlacedCardContext(timeStart = Clock.System.now()).run {
        try {
            receiveThenFromTransport(this)
            this@process.exec(this)
            toTransportThenRespond(this)
        } catch (e: Throwable) {
            state = FcState.FAILING
            errors.add(e.asFcError())
            this@process.exec(this)
            toTransportThenRespond(this)
        }
    }
}