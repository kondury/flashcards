package com.github.kondury.flashcards.cards.app

import com.github.kondury.flashcards.cards.app.helpers.testConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplicationTest {

    private val appConfig = testConfig()

    @Test
    fun `root endpoint`() = testApplication {
        application { moduleJvm(appConfig) }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Cards-app", response.bodyAsText())
    }
}
