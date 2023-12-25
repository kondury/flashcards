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
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig
import io.ktor.server.config.*


data class PlacedCardsKtorConfig(
    private val settings: PlacedCardsKtorSettings,
) : PlacedCardsApplicationConfig by object : PlacedCardsApplicationConfig {
    override val loggerProvider = initLoggerProvider(settings.loggerSettings)
    override val repositoryConfig = initRepositoryConfig(settings.repositorySettings)
    override val corConfig = PlacedCardsCorConfig(repositoryConfig)
    override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
    override val auth: AuthConfig = initAuthConfig(settings.authSettings)
} {
    constructor(config: ApplicationConfig) : this(settings = PlacedCardsKtorSettings(config))
}

private fun initLoggerProvider(loggerSettings: LoggerSettings): AppLoggerProvider =
    when (loggerSettings.mode) {
        "logback", "" -> AppLoggerProvider { getLogbackLogger(it) }
        else -> throw Exception("Logger '${loggerSettings.mode}' is not allowed. Admitted value is 'logback'")
    }

private fun initRepositoryConfig(settings: RepositorySettings) = with(settings) {
    PlacedCardRepositoryConfig(
        prodRepository = initRepository(prodRepositoryType),
        testRepository = initRepository(testRepositoryType)
    )
}

private fun initAuthConfig(settings: AuthSettings) = AuthConfig(
    secret = settings.secret,
    issuer = settings.issuer,
    audience = settings.audience,
    realm = settings.realm,
    clientId = settings.clientId,
    certUrl = settings.certUrl,
)

private fun RepositorySettings.initRepository(type: RepositoryType): PlacedCardRepository =
    when (type) {
        IN_MEMORY -> initInMemory(inMemorySettings)
        POSTGRES -> initPostgres(postgresSettings)
    }

private fun initPostgres(settings: PostgresSettings) = PostgresPlacedCardRepository(
    properties = SqlProperties(
        url = settings.url,
        user = settings.user,
        password = settings.password,
        schema = settings.schema,
    )
)

private fun initInMemory(settings: InMemorySettings) = InMemoryPlacedCardRepository(ttl = settings.ttl)