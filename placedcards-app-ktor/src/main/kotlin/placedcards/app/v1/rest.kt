package com.github.kondury.flashcards.placedcards.app.v1

import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Route.v1PlacedCard(config: PlacedCardsApplicationConfig) {
    route("placed-card") {
        post("create") {
            call.createPlacedCard(config)
        }
        post("move") {
            call.movePlacedCard(config)
        }
        post("delete") {
            call.deletePlacedCard(config)
        }
        post("select") {
            call.selectPlacedCard(config)
        }
        post("init") {
            call.initPlacedCard(config)
        }
    }
}