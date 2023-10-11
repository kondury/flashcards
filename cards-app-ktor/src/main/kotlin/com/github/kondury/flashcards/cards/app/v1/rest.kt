package com.github.kondury.flashcards.cards.app.v1

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Route.v1Card(processor: FcCardProcessor) {
    route("card") {
        post("create") {
            call.createCard(processor)
        }
        post("read") {
            call.readCard(processor)
        }
        post("delete") {
            call.deleteCard(processor)
        }
    }
}