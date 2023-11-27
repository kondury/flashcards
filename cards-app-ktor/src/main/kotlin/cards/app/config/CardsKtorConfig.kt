package com.github.kondury.flashcards.cards.app.config

import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.repository.inmemory.InMemoryCardRepository
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import io.ktor.server.config.*


data class CardsKtorConfig(
    private val settings: CardsKtorSettings,
) : CardsApplicationConfig by object : CardsApplicationConfig {
    override val loggerProvider = getLoggerProvider(settings)
    override val repositoryConfig = CardRepositoryConfig (
        prodRepository = InMemoryCardRepository(),
        testRepository = InMemoryCardRepository()
    )
    override val corConfig: CardsCorConfig = CardsCorConfig(repositoryConfig)
    override val processor: FcCardProcessor = FcCardProcessor(corConfig)
} {
    constructor(config: ApplicationConfig) : this(settings = CardsKtorSettings(config))
}

private fun getLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }