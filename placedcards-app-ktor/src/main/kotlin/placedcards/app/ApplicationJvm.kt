package com.github.kondury.flashcards.placedcards.app

import com.auth0.jwt.JWT
import com.github.kondury.flashcards.placedcards.app.base.resolveAlgorithm
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig.Companion.GROUPS_CLAIM
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.app.config.PlacedCardsKtorConfig
import com.github.kondury.flashcards.placedcards.app.plugins.initPluginsJvm
import com.github.kondury.flashcards.placedcards.app.v1.v1PlacedCard
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.reflect.jvm.jvmName

private val loggerId = Application::moduleJvm::class.jvmName.substringBefore("$")

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// Referenced in application.yml
fun Application.moduleJvm(config: PlacedCardsApplicationConfig = PlacedCardsKtorConfig(environment.config)) {
    val logger = config.loggerProvider.logger(loggerId)
    initPluginsJvm(logger)

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = config.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT.require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@moduleJvm.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        get("/") {
            call.respondText("PlacedCards-app")
        }
        route("v1") {
            authenticate("auth-jwt") {
                v1PlacedCard(config)
            }
        }
    }
}
