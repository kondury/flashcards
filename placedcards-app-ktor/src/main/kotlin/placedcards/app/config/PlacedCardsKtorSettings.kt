package com.github.kondury.flashcards.placedcards.app.config

import io.ktor.server.config.*

data class PlacedCardsKtorSettings(
    val appUrls: List<String>,
    override val mode: String,
) : LoggerSettings {

    constructor(config: ApplicationConfig) : this(
        appUrls = config.propertyOrNull("ktor.urls")?.getList().orEmpty(),
        mode = config.propertyOrNull("ktor.logger")?.getString().orEmpty(),
    )
}
