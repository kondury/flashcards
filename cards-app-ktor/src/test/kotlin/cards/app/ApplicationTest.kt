package com.github.kondury.flashcards.cards.app

import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ApplicationTest {

    private val appConfig = object : CardsApplicationConfig {
        override val loggerProvider = AppLoggerProvider()
        override val repositoryConfig = CardRepositoryConfig(
            prodRepository = CardRepository.NoOpCardRepository,
            testRepository = CardRepository.NoOpCardRepository
        )
        override val corConfig: CardsCorConfig = CardsCorConfig(repositoryConfig)
        override val processor: FcCardProcessor = FcCardProcessor(corConfig)
    }

    @Test
    fun `root endpoint`() = testApplication {
        application { moduleJvm(appConfig) }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Cards-app", response.bodyAsText())
    }
}
