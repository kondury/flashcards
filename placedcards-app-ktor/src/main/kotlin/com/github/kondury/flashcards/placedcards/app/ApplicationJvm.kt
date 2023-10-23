package com.github.kondury.flashcards.placedcards.app

import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.placedcards.app.v1.v1PlacedCard
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm() {
    val processor = FcPlacedCardProcessor()

    install(CallLogging) {
        level = Level.INFO
    }

    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }

    routing {
        get("/") {
            call.respondText("PlacedCards-app")
        }
        route("v1") {
            v1PlacedCard(processor)
        }
    }
}
