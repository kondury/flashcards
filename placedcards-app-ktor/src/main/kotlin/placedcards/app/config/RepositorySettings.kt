package com.github.kondury.flashcards.placedcards.app.config

interface RepositorySettings {
    val prodRepositoryType: RepositoryType
    val testRepositoryType: RepositoryType
    val postgresSettings: PostgresSettings
    val inMemorySettings: InMemorySettings
}

object RepositorySettingsPaths {
    private const val APP_ROOT = "placed-cards"
    const val REPOSITORY_PATH = "$APP_ROOT.repository"
    const val IN_MEMORY_PATH = "$REPOSITORY_PATH.in-memory"
    const val POSTGRES_PATH = "$REPOSITORY_PATH.psql"
}

enum class WorkModeRepository(val path: String) {
    PROD("prod"), TEST("test")
}

enum class RepositoryType(val synonyms: List<String>) {
    IN_MEMORY(listOf("in-memory", "inmemory", "memory", "mem")),
    POSTGRES(listOf("postgres", "postgresql", "pg", "sql", "psql"));

    companion object {
        operator fun get(type: String) = when (type.lowercase()) {
            in IN_MEMORY.synonyms -> IN_MEMORY
            in POSTGRES.synonyms -> POSTGRES
            else -> {
                val typeValues = entries.joinToString { "'${it.synonyms.first()}'" }
                throw IllegalArgumentException(
                    "Unsupported repository type $type: type could be one of $typeValues." +
                            " Check value in your application.yml "
                )
            }
        }
    }
}