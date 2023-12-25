package com.github.kondury.flashcards.placedcards.app

import com.github.kondury.flashcards.placedcards.app.helpers.testConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


private val appConfig = testConfig()

class ApplicationTest {
    @Test
    fun `root endpoint`() = testApplication {
        application { moduleJvm(appConfig) }
        val response = client.get("/")
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals("PlacedCards-app", response.bodyAsText())
    }
}
