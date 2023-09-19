package com.github.kondury.flashcards.blackbox.test.action.v1

import io.kotest.assertions.withClue
import io.kotest.matchers.should
import com.github.kondury.flashcards.blackbox.fixture.client.Client

suspend fun Client.createReview(): Unit =
    withClue("createReviewV1") {
        val response = sendAndReceive(
            "review/create", """
                {
                    "box": "New"
                    "strategy": "First in Order"
                }
            """.trimIndent()
        )

        response should haveNoErrors
    }
