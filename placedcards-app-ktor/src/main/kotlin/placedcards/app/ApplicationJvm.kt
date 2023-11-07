package com.github.kondury.flashcards.placedcards.app

import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.app.config.PlacedCardsKtorConfig
import com.github.kondury.flashcards.placedcards.app.plugins.initPluginsJvm
import com.github.kondury.flashcards.placedcards.app.v1.v1PlacedCard
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.reflect.jvm.jvmName

private val loggerId = Application::moduleJvm::class.jvmName.substringBefore("$")

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(config: PlacedCardsApplicationConfig = PlacedCardsKtorConfig(environment.config)) {
    val logger = config.loggerProvider.logger(loggerId)
    initPluginsJvm(logger)

    routing {
        get("/") {
            call.respondText("PlacedCards-app")
        }
        route("v1") {
            v1PlacedCard(config)
        }
    }
}
