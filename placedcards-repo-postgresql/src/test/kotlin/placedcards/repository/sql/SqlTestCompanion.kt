package com.github.kondury.flashcards.placedcards.repository.sql

import com.benasher44.uuid.uuid4
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "placedcards-pass"
    private const val SCHEMA = "placedcards"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        test: String,
        initObjects: Collection<PlacedCard> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ) = PostgresPlacedCardRepository(
            SqlProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
                table = "placedcards-$test",
            ),
            initObjects,
            randomUuid = randomUuid
        )
}
