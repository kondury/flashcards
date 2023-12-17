package com.github.kondury.flashcards.placedcards.repository.sql

data class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/placedcards",
    val user: String = "postgres",
    val password: String = "placedcards-pass",
    val schema: String = "placedcards",
    val table: String = "placedcards",
)