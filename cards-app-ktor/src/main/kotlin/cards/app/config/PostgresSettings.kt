package com.github.kondury.flashcards.cards.app.config

interface PostgresSettings {
    val url: String
    val user: String
    val password: String
    val schema: String
}