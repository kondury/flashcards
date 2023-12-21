package com.github.kondury.flashcards.placedcards.app.auth

import com.github.kondury.flashcards.placedcards.app.helpers.authConfig
import com.github.kondury.flashcards.placedcards.app.helpers.testConfig
import com.github.kondury.flashcards.placedcards.app.moduleJvm
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        val config = testConfig()
        application { moduleJvm(config) }
        val response = client.post("/v1/placed-card/create") {
            addAuth(config = authConfig.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
