package com.github.kondury.flashcards.placedcards.app.config

import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.app.config.RepositoryType.IN_MEMORY
import com.github.kondury.flashcards.placedcards.app.config.RepositoryType.POSTGRES
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import com.github.kondury.flashcards.placedcards.repository.inmemory.InMemoryPlacedCardRepository
import com.github.kondury.flashcards.placedcards.repository.sql.PostgresPlacedCardRepository
import com.github.kondury.flashcards.placedcards.repository.sql.SqlProperties
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import io.ktor.server.config.*


data class PlacedCardsKtorConfig(
    private val settings: PlacedCardsKtorSettings,
) : PlacedCardsApplicationConfig by object : PlacedCardsApplicationConfig {
        override val loggerProvider = getLoggerProvider(settings)
        override val repositoryConfig = PlacedCardRepositoryConfig(
            prodRepository = getRepositoryConfig(settings.prodRepositoryType, settings),
            testRepository = getRepositoryConfig(settings.testRepositoryType, settings),
        )
        override val corConfig = PlacedCardsCorConfig(repositoryConfig)
        override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
} {
    constructor(config: ApplicationConfig) : this(settings = PlacedCardsKtorSettings(config))
}

private fun getLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }

private fun getRepositoryConfig(type: RepositoryType, settings: RepositorySettings): PlacedCardRepository =
    when (type) {
        IN_MEMORY -> initInMemory(settings.inMemorySettings)
        POSTGRES -> initPostgres(settings.postgresSettings)
    }

private fun initPostgres(settings: PostgresSettings): PlacedCardRepository = PostgresPlacedCardRepository(
    properties = SqlProperties(
        url = settings.url,
        user = settings.user,
        password = settings.password,
        schema = settings.schema,
    )
)

private fun initInMemory(settings: InMemorySettings): PlacedCardRepository = InMemoryPlacedCardRepository(ttl = settings.ttl)