package com.github.kondury.flashcards.cor.handlers

/**
 * Base executor in CoR having title and description
 */
interface CorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}
