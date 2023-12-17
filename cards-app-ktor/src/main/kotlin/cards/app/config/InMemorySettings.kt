package com.github.kondury.flashcards.cards.app.config

import kotlin.time.Duration

interface InMemorySettings {
    val ttl: Duration
}