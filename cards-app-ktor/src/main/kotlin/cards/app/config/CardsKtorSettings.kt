package com.github.kondury.flashcards.cards.app.config

import com.github.kondury.flashcards.cards.app.config.SettingPaths.IN_MEMORY_PATH
import com.github.kondury.flashcards.cards.app.config.SettingPaths.JWT_PATH
import com.github.kondury.flashcards.cards.app.config.SettingPaths.POSTGRES_PATH
import com.github.kondury.flashcards.cards.app.config.SettingPaths.REPOSITORY_PATH
import io.ktor.server.config.*
import kotlin.time.Duration


data class CardsKtorSettings(
    val appUrls: List<String>,
    val loggerSettings: LoggerSettings,
    val repositorySettings: RepositorySettings,
    val authSettings: AuthSettings
) {
    constructor(config: ApplicationConfig) : this(
        appUrls = config.propertyOrNull("ktor.urls")?.getList().orEmpty(),
        loggerSettings = object : LoggerSettings {
            override val mode = config.propertyOrNull("ktor.logger")?.getString().orEmpty()
        },
        repositorySettings = getRepositorySettings(config),
        authSettings = getAuthSettings(config)
    )
}

private fun getRepositorySettings(config: ApplicationConfig) = object : RepositorySettings {
    override val prodRepositoryType = getRepositoryType(config, WorkModeRepository.PROD)
    override val testRepositoryType = getRepositoryType(config, WorkModeRepository.TEST)
    override val postgresSettings = getPostgresSettings(config)
    override val inMemorySettings = getInMemorySettings(config)
}

private fun getRepositoryType(config: ApplicationConfig, mode: WorkModeRepository): RepositoryType {
    val repositoryModePath = "$REPOSITORY_PATH.${mode.path}"
    val type = config.propertyOrNull(repositoryModePath)?.getString() ?: "in-memory"
    return RepositoryType[type]
}

private fun getPostgresSettings(config: ApplicationConfig) = object : PostgresSettings {
    override val url = config.propertyOrDefault(
        "$POSTGRES_PATH.url",
        "jdbc:postgresql://localhost:5432/cards"
    )
    override val user = config.propertyOrDefault("$POSTGRES_PATH.user", "postgres")
    override val password = config.propertyOrDefault("$POSTGRES_PATH.password", "cards-pass")
    override val schema = config.propertyOrDefault("$POSTGRES_PATH.schema", "cards")
}

private fun getInMemorySettings(config: ApplicationConfig) = object : InMemorySettings {
    override val ttl = config.propertyOrDefault("$IN_MEMORY_PATH.ttl", "10m")
        .let { Duration.parse(it) }
}

private fun getAuthSettings(config: ApplicationConfig): AuthSettings = object : AuthSettings {
    override val secret = config.propertyOrDefault("$JWT_PATH.secret", "")
    override val issuer = config.property("$JWT_PATH.issuer").getString()
    override val audience = config.property("$JWT_PATH.audience").getString()
    override val realm = config.property("$JWT_PATH.realm").getString()
    override val clientId = config.property("$JWT_PATH.clientId").getString()
    override val certUrl = config.propertyOrNull("$JWT_PATH.certUrl")?.getString()
}

    private fun ApplicationConfig.propertyOrDefault(path: String, defaultValue: String) =
        propertyOrNull(path)
            ?.getString()
            ?: defaultValue
