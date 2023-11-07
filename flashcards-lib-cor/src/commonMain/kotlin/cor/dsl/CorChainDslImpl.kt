package com.github.kondury.flashcards.cor.dsl

import com.github.kondury.flashcards.cor.handlers.CorChain
import com.github.kondury.flashcards.cor.handlers.CorExec

@CorDslMarker
class CorChainDslImpl<T> : AbstractCorExecDsl<T>(), CorChainDsl<T> {
    private val workers: MutableList<CorExecDsl<T>> = mutableListOf()
    override fun add(worker: CorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): CorExec<T> = CorChain(
        title = title,
        description = description,
        executors = workers.map { it.build() },
        blockActiveIf = blockActiveIf,
        blockOnException = blockOnException
    )
}
