package com.github.kondury.flashcards.cards.app

import com.github.kondury.flashcards.cards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.cards.app.v1.v1Card
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
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
    val processor = FcCardProcessor()

    install(CallLogging) {
        level = Level.INFO
    }

    install(ContentNegotiation) {
        jackson {
//            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//            enable(SerializationFeature.INDENT_OUTPUT)
//            writerWithDefaultPrettyPrinter()
//            setSerializationInclusion(JsonInclude.Include.NON_NULL)
//            registerModule(JavaTimeModule())
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }

    routing {
        get("/") {
            call.respondText("Cards-app")
        }
        route("v1") {
            v1Card(processor)
        }
    }
}
