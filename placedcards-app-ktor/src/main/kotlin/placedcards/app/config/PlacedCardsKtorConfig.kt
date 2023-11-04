package com.github.kondury.flashcards.placedcards.app.config

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import io.ktor.server.config.*


data class PlacedCardsKtorConfig(
    private val settings: PlacedCardsKtorSettings,
) : PlacedCardsApplicationConfig by object : PlacedCardsApplicationConfig {
    override val loggerProvider: AppLoggerProvider = getLoggerProvider(settings)
    override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor()
} {
    constructor(config: ApplicationConfig) : this(settings = PlacedCardsKtorSettings(config))
}

// todo move to flashcards-app-common or to flashcards-app-ktor
private fun getLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }