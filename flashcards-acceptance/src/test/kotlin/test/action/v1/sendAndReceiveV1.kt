package com.github.kondury.flashcards.blackbox.test.action.v1

import com.github.kondury.flashcards.blackbox.fixture.client.Client
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

suspend fun Client.sendAndReceive(path: String, requestBody: String): String {
    logger.info { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    logger.info { "Received\n$responseBody" }

    return responseBody
}