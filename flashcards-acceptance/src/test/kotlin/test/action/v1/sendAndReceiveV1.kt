package com.github.kondury.flashcards.blackbox.test.action.v1

import mu.KotlinLogging
import com.github.kondury.flashcards.blackbox.fixture.client.Client

private val log = KotlinLogging.logger {}

suspend fun Client.sendAndReceive(path: String, requestBody: String): String {
    log.info { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.info { "Received\n$responseBody" }

    return responseBody
}