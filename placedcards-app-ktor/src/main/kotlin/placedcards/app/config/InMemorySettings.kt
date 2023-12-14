package com.github.kondury.flashcards.placedcards.app.config

import kotlin.time.Duration

interface InMemorySettings {
    val ttl: Duration
}