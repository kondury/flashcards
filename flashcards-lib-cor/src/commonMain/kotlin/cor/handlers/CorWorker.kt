package com.github.kondury.flashcards.cor.handlers

class CorWorker<T>(
    title: String,
    description: String = "",
    blockActiveIf: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    blockOnException: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockActiveIf, blockOnException) {
    override suspend fun handle(context: T) = blockHandle(context)
}
