package com.github.kondury.flashcards.cor.dsl

import com.github.kondury.flashcards.cor.handlers.CorExec
import com.github.kondury.flashcards.cor.handlers.CorWorker

@CorDslMarker
class CorWorkerDslImpl<T> : AbstractCorExecDsl<T>(), CorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(handler: suspend T.() -> Unit) {
        blockHandle = handler
    }

    override fun build(): CorExec<T> = CorWorker(
        title = title,
        description = description,
        blockActiveIf = blockActiveIf,
        blockHandle = blockHandle,
        blockOnException = blockOnException
    )
}