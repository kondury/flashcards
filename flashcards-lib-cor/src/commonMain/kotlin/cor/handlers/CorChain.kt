package com.github.kondury.flashcards.cor.handlers

/**
 * Chain of Responsibility implementation. It executes embedded chains and workers sequentially
 * according the order they were added into executors list.
 */
class CorChain<T>(
    private val executors: List<CorExec<T>>,
    title: String,
    description: String = "",
    blockActiveIf: suspend T.() -> Boolean = { true },
    blockOnException: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockActiveIf, blockOnException) {
    override suspend fun handle(context: T) {
        executors.forEach { it.exec(context) }
    }
}



