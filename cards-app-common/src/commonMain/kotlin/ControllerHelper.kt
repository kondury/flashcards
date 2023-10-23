package com.github.kondury.flashcards.cards.app.common

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.helpers.asFcError
import com.github.kondury.flashcards.cards.common.models.FcState
import kotlinx.datetime.Clock


suspend inline fun FcCardProcessor.process(
    noinline receiveThenFromTransport: suspend (CardContext) -> Unit,
    noinline toTransportThenRespond: suspend (CardContext) -> Unit,
) {
    CardContext(timeStart = Clock.System.now()).run {
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