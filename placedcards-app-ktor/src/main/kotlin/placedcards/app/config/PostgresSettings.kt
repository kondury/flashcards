package com.github.kondury.flashcards.placedcards.app.config

interface PostgresSettings {
    val url: String
    val user: String
    val password: String
    val schema: String
}