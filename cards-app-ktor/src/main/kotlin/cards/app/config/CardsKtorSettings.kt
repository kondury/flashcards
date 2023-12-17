package com.github.kondury.flashcards.cards.app.config

import com.github.kondury.flashcards.cards.app.config.RepositorySettingsPaths.IN_MEMORY_PATH
import com.github.kondury.flashcards.cards.app.config.RepositorySettingsPaths.POSTGRES_PATH
import io.ktor.server.config.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class CardsKtorSettings(
    val appUrls: List<String>,
    private val loggerSettings: LoggerSettings,
    private val repositorySettings: RepositorySettings
) : LoggerSettings by loggerSettings,
    RepositorySettings by repositorySettings {

    constructor(config: ApplicationConfig) : this(
        appUrls = config.propertyOrNull("ktor.urls")?.getList().orEmpty(),
        loggerSettings = object : LoggerSettings {
            override val mode = config.propertyOrNull("ktor.logger")?.getString().orEmpty()
        },
        repositorySettings = getRepositorySettings(config)
    )
}

fun getRepositorySettings(config: ApplicationConfig) = object : RepositorySettings {
    override val prodRepositoryType = getRepositoryType(config, WorkModeRepository.PROD)
    override val testRepositoryType = getRepositoryType(config, WorkModeRepository.TEST)
    override val postgresSettings = getPostgresSettings(config)
    override val inMemorySettings = getInMemorySettings(config)
}

fun getRepositoryType(config: ApplicationConfig, mode: WorkModeRepository): RepositoryType {
    val repositoryModePath = "${RepositorySettingsPaths.REPOSITORY_PATH}.${mode.path}"
    val type = config.propertyOrNull(repositoryModePath)?.getString() ?: "in-memory"
    return RepositoryType[type]
}

fun getPostgresSettings(config: ApplicationConfig) = object : PostgresSettings {
    override val url = config.propertyOrDefault(
        "$POSTGRES_PATH.url",
        "jdbc:postgresql://localhost:5432/cards"
    )
    override val user = config.propertyOrDefault("$POSTGRES_PATH.user", "postgres")
    override val password = config.propertyOrDefault("$POSTGRES_PATH.password", "cards-pass")
    override val schema = config.propertyOrDefault("$POSTGRES_PATH.schema", "cards")

    private fun ApplicationConfig.propertyOrDefault(path: String, defaultValue: String) =
        propertyOrNull(path)
            ?.getString()
            ?: defaultValue
}

fun getInMemorySettings(config: ApplicationConfig) = object : InMemorySettings {
    override val ttl = config.propertyOrNull("$IN_MEMORY_PATH.ttl")
        ?.getString()
        ?.let { Duration.parse(it) }
        ?: 10.minutes
}