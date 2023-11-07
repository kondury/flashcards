package com.github.kondury.flashcards.placedcards.app

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ApplicationTest {
    @Test
    fun `root endpoint`() = testApplication {
        val response = client.get("/")
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals("PlacedCards-app", response.bodyAsText())
    }
}