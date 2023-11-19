package com.github.kondury.flashcards.cor.dsl

/**
 * Chains DSL entry point.
 * All elements are executed sequentially.
 *
 * Example:
 * ```
 * rootChain<SomeContext> {
 *     worker {
 *     }
 *     chain {
 *         worker(...) {
 *         }
 *         worker(...) {
 *         }
 *     }
 *     parallel {
 *        ...
 *     }
 *  }
 * ```
 */
fun <T> rootChain(function: CorChainDsl<T>.() -> Unit): CorChainDsl<T> = CorChainDslImpl<T>().apply(function)

/**
 * Create a chain
 */
fun <T> CorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDslImpl<T>().apply(function))
}

/**
 * Create a worker
 */
fun <T> CorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDslImpl<T>().apply(function))
}

/**
 * Create worker with default 'activeIf' and 'onException' methods
 */
fun <T> CorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDslImpl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}