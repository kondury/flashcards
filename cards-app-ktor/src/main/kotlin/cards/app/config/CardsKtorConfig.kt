package com.github.kondury.flashcards.cards.app.config

import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import io.ktor.server.config.*


data class CardsKtorConfig(
    private val settings: CardsKtorSettings,
) : CardsApplicationConfig by object : CardsApplicationConfig {
    override val loggerProvider: AppLoggerProvider = getLoggerProvider(settings)
    override val processor: FcCardProcessor = FcCardProcessor()
} {
    constructor(config: ApplicationConfig) : this(settings = CardsKtorSettings(config))
}

private fun getLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }