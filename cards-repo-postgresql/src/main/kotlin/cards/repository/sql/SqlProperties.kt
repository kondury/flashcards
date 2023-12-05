package com.github.kondury.flashcards.cards.repository.sql

data class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/cards",
    val user: String = "postgres",
    val password: String = "cards-pass",
    val schema: String = "cards",
    val table: String = "cards",
)