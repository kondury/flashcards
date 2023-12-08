package com.github.kondury.flashcards.placedcards.app.config

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.repository.inmemory.InMemoryPlacedCardRepository
import io.ktor.server.config.*


data class PlacedCardsKtorConfig(
    private val settings: PlacedCardsKtorSettings,
) : PlacedCardsApplicationConfig by object : PlacedCardsApplicationConfig {
        override val loggerProvider: AppLoggerProvider = AppLoggerProvider { getLogbackLogger(it) }
        override val repositoryConfig = PlacedCardRepositoryConfig(
            prodRepository = InMemoryPlacedCardRepository(),
            testRepository = InMemoryPlacedCardRepository(),
        )
        override val corConfig = PlacedCardsCorConfig(repositoryConfig)
        override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
} {
    constructor(config: ApplicationConfig) : this(settings = PlacedCardsKtorSettings(config))
}

// todo move to flashcards-app-common
private fun getLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }