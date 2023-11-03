package com.github.kondury.flashcards.cards.app.v1

import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Route.v1Card(config: CardsApplicationConfig) {
    route("card") {
        post("create") {
            call.createCard(config)
        }
        post("read") {
            call.readCard(config)
        }
        post("delete") {
            call.deleteCard(config)
        }
    }
}