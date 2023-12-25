package com.github.kondury.flashcards.cards.app.v1

import com.github.kondury.flashcards.cards.api.v1.models.IRequest
import com.github.kondury.flashcards.cards.app.base.toModel
import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.mappers.v1.fromTransport
import com.github.kondury.flashcards.cards.mappers.v1.toTransportCard
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private val loggerId = {}.javaClass.name.substringBefore("Kt$")

suspend fun ApplicationCall.createCard(config: CardsApplicationConfig) = processV1(config, "httpCreateCard")

suspend fun ApplicationCall.readCard(config: CardsApplicationConfig) = processV1(config, "httpReadCard")

suspend fun ApplicationCall.deleteCard(config: CardsApplicationConfig) = processV1(config, "httpDeleteCard")

internal suspend inline fun ApplicationCall.processV1(config: CardsApplicationConfig, logId: String) {
    val processor = config.processor
    val logger = config.loggerProvider.logger(loggerId)
    processor.process(
        { cardContext ->
            cardContext.principal = this@processV1.request.call.principal<JWTPrincipal>().toModel()
            cardContext.fromTransport(receive<IRequest>())
        },
        { cardContext -> respond(cardContext.toTransportCard()) },
        logger,
        logId
    )
}
