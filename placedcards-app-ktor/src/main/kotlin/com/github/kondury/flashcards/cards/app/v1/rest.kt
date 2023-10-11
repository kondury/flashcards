package com.github.kondury.flashcards.placedcards.app.v1

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Route.v1PlacedCard(processor: FcPlacedCardProcessor) {
    route("placed-card") {
        post("create") {
            call.createPlacedCard(processor)
        }
        post("move") {
            call.movePlacedCard(processor)
        }
        post("delete") {
            call.deletePlacedCard(processor)
        }
        post("select") {
            call.selectPlacedCard(processor)
        }
        post("init") {
            call.initPlacedCard(processor)
        }
    }
}