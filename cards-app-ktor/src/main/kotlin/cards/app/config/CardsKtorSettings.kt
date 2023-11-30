package com.github.kondury.flashcards.cards.app.config

import io.ktor.server.config.*

data class CardsKtorSettings(
    val appUrls: List<String>,
    override val mode: String,
) : LoggerSettings {

    constructor(config: ApplicationConfig) : this(
        appUrls = config.propertyOrNull("ktor.urls")?.getList().orEmpty(),
        mode = config.propertyOrNull("ktor.logger")?.getString().orEmpty(),
    )
}
