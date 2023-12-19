package com.github.kondury.flashcards.cards.app.config

interface AuthSettings {
    val secret: String
    val issuer: String
    val audience: String
    val realm: String
    val clientId: String
    val certUrl: String? get() = null
}
