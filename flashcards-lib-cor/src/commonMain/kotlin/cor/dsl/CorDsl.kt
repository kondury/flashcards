package com.github.kondury.flashcards.cor.dsl

import com.github.kondury.flashcards.cor.handlers.CorExec

@DslMarker
annotation class CorDslMarker

/**
 * DSL base builder
 */
@CorDslMarker
interface CorExecDsl<T> {
    var title: String
    var description: String
    fun activeIf(condition: suspend T.() -> Boolean)
    fun onException(handler: suspend T.(e: Throwable) -> Unit)

    fun build(): CorExec<T>
}

/**
 * DSL Chain builder
 */
@CorDslMarker
interface CorChainDsl<T> : CorExecDsl<T> {
    fun add(worker: CorExecDsl<T>)
}

/**
 * DSL Worker builder
 */
@CorDslMarker
interface CorWorkerDsl<T> : CorExecDsl<T> {
    fun handle(handler: suspend T.() -> Unit)
}


