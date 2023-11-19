package com.github.kondury.flashcards.cor.dsl

abstract class AbstractCorExecDsl<T> : CorExecDsl<T> {
    protected var blockActiveIf: suspend T.() -> Boolean = { true }
    protected var blockOnException: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun activeIf(condition: suspend T.() -> Boolean) {
        blockActiveIf = condition
    }

    override fun onException(handler: suspend T.(e: Throwable) -> Unit) {
        blockOnException = handler
    }
}