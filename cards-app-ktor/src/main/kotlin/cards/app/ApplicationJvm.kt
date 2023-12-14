package com.github.kondury.flashcards.cards.app

import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.app.config.CardsKtorConfig
import com.github.kondury.flashcards.cards.app.plugins.initPluginsJvm
import com.github.kondury.flashcards.cards.app.v1.v1Card
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.reflect.jvm.jvmName

private val loggerId = Application::moduleJvm::class.jvmName.substringBefore("$")

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// Referenced in application.yml
fun Application.moduleJvm(config: CardsApplicationConfig = CardsKtorConfig(environment.config)) {
    val logger = config.loggerProvider.logger(loggerId)
    initPluginsJvm(logger)

    routing {
        get("/") {
            call.respondText("Cards-app")
        }
        route("v1") {
            v1Card(config)
        }
    }
}
