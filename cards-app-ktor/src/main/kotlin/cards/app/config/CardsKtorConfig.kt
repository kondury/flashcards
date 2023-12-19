package com.github.kondury.flashcards.cards.app.config

import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.app.config.RepositoryType.IN_MEMORY
import com.github.kondury.flashcards.cards.app.config.RepositoryType.POSTGRES
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.inmemory.InMemoryCardRepository
import com.github.kondury.flashcards.cards.repository.sql.PostgresCardRepository
import com.github.kondury.flashcards.cards.repository.sql.SqlProperties
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.logging.jvm.getLogbackLogger
import io.ktor.server.config.*


data class CardsKtorConfig(
    private val settings: CardsKtorSettings,
) : CardsApplicationConfig by object : CardsApplicationConfig {
    override val loggerProvider = initLoggerProvider(settings.loggerSettings)
    override val repositoryConfig = initRepositoryConfig(settings.repositorySettings)
    override val corConfig: CardsCorConfig = CardsCorConfig(repositoryConfig)
    override val processor: FcCardProcessor = FcCardProcessor(corConfig)
} {
    constructor(config: ApplicationConfig) : this(settings = CardsKtorSettings(config))
}

private fun initLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }

private fun initRepositoryConfig(settings: RepositorySettings) = with(settings) {
    CardRepositoryConfig(
        prodRepository = initRepository(prodRepositoryType),
        testRepository = initRepository(testRepositoryType)
    )
}

private fun RepositorySettings.initRepository(type: RepositoryType): CardRepository =
    when (type) {
        IN_MEMORY -> initInMemory(inMemorySettings)
        POSTGRES -> initPostgres(postgresSettings)
    }

private fun initPostgres(settings: PostgresSettings) = PostgresCardRepository(
    properties = SqlProperties(
        url = settings.url,
        user = settings.user,
        password = settings.password,
        schema = settings.schema,
    )
)

private fun initInMemory(settings: InMemorySettings) = InMemoryCardRepository(ttl = settings.ttl)
