package com.github.kondury.flashcards.placedcards.app.config

import com.github.kondury.flashcards.placedcards.app.config.RepositorySettingsPaths.IN_MEMORY_PATH
import com.github.kondury.flashcards.placedcards.app.config.RepositorySettingsPaths.POSTGRES_PATH
import io.ktor.server.config.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class PlacedCardsKtorSettings(
    val appUrls: List<String>,
    private val loggerSettings: LoggerSettings,
    private val repositorySettings: RepositorySettings,
) : LoggerSettings by loggerSettings,
    RepositorySettings by repositorySettings {

    constructor(config: ApplicationConfig) : this(
        appUrls = config.propertyOrNull("ktor.urls")?.getList().orEmpty(),
        loggerSettings = object : LoggerSettings {
            override val mode: String = config.propertyOrNull("ktor.logger")?.getString().orEmpty()
        },
        repositorySettings = getRepositorySettings(config)
    )
}

private fun getRepositorySettings(config: ApplicationConfig) = object : RepositorySettings {
    override val prodRepositoryType = getRepositoryType(config, WorkModeRepository.PROD)
    override val testRepositoryType = getRepositoryType(config, WorkModeRepository.TEST)
    override val postgresSettings = getPostgresSettings(config)
    override val inMemorySettings = getInMemorySettings(config)
}

private fun getRepositoryType(config: ApplicationConfig, mode: WorkModeRepository): RepositoryType {
    val repositoryModePath = "${RepositorySettingsPaths.REPOSITORY_PATH}.${mode.path}"
    val type = config.propertyOrNull(repositoryModePath)?.getString() ?: "in-memory"
    return RepositoryType[type]
}

private fun getPostgresSettings(config: ApplicationConfig) = object : PostgresSettings {
    override val url = config.propertyOrDefault(
        "$POSTGRES_PATH.url",
        "jdbc:postgresql://localhost:5432/placedcards"
    )
    override val user = config.propertyOrDefault("$POSTGRES_PATH.user", "postgres")
    override val password = config.propertyOrDefault("$POSTGRES_PATH.password", "placedcards-pass")
    override val schema = config.propertyOrDefault("$POSTGRES_PATH.schema", "placedcards")

    private fun ApplicationConfig.propertyOrDefault(path: String, defaultValue: String) =
        propertyOrNull(path)
            ?.getString()
            ?: defaultValue
}

private fun getInMemorySettings(config: ApplicationConfig) = object : InMemorySettings {
    override val ttl = config.propertyOrNull("$IN_MEMORY_PATH.ttl")
        ?.getString()
        ?.let { Duration.parse(it) }
        ?: 10.minutes
}
