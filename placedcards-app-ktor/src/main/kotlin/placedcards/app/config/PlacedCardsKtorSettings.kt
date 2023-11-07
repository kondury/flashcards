package com.github.kondury.flashcards.placedcards.app.config

import io.ktor.server.config.*

// todo move common settings to flashcards-app-ktor
data class PlacedCardsKtorSettings(
    val appUrls: List<String>,
    override val mode: String,
) : LoggerSettings {

    constructor(config: ApplicationConfig) : this(
        appUrls = config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        mode = config.propertyOrNull("ktor.logger")?.getString() ?: "",
    )
}
