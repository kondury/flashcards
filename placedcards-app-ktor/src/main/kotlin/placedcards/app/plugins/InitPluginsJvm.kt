package com.github.kondury.flashcards.placedcards.app.plugins

import com.github.kondury.flashcards.logging.common.AppLogger
import com.github.kondury.flashcards.logging.jvm.LogbackWrapper
import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import org.slf4j.event.Level

// todo extract common ktor plugins installation and move it to flashcards-app-ktor
fun Application.initPluginsJvm(appLogger: AppLogger) {

//    install(Routing)
//    install(CORS) {
//        allowNonSimpleContentTypes = true
//        allowSameOrigin = true
//        allowMethod(HttpMethod.Options)
//        allowMethod(HttpMethod.Post)
//        allowMethod(HttpMethod.Get)
//        allowHeader("*")
//        appUrls.forEach {
//            val split = it.split("://")
//            println("$split")
//            when (split.size) {
//                2 -> allowHost(
//                    split[1].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
//                    listOf(split[0])
//                )
//
//                1 -> allowHost(
//                    split[0].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
//                    listOf("http", "https")
//                )
//            }
//        }
//    }
//    install(CachingHeaders)
//    install(AutoHeadResponse)

    install(CallLogging) {
        level = Level.INFO
        logger = (appLogger as? LogbackWrapper)?.logger
    }

    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }
}