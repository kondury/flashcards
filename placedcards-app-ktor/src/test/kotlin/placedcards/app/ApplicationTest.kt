package com.github.kondury.flashcards.placedcards.app

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private val appConfig = object : PlacedCardsApplicationConfig {
    override val loggerProvider = AppLoggerProvider()
    override val repositoryConfig = PlacedCardRepositoryConfig(
        prodRepository = PlacedCardRepository.NoOpPlacedCardRepository,
        testRepository = PlacedCardRepository.NoOpPlacedCardRepository,
    )
    override val corConfig: PlacedCardsCorConfig = PlacedCardsCorConfig(repositoryConfig)
    override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
}

class ApplicationTest {
    @Test
    fun `root endpoint`() = testApplication {
        application { moduleJvm(appConfig) }
        val response = client.get("/")
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals("PlacedCards-app", response.bodyAsText())
    }
}
