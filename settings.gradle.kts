rootProject.name = "flashcards"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings

    val ktorVersion: String by settings

//    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false

//        id("com.github.johnrengelman.shadow") version pluginShadow apply false
//
//        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
//        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false

    }
}

include("flashcards-acceptance")
include("flashcards-app-kafka")
include("flashcards-app-rabbit")

include("flashcards-lib-logging-common")
include("flashcards-lib-logging-logback")
include("flashcards-lib-cor")

include("cards-api-log")
include("cards-api-v1-jackson")
include("cards-common")
include("cards-mappers-log")
include("cards-mappers-v1")
include("cards-stubs")
include("cards-biz")
include("cards-app-ktor")
include("cards-app-kafka")
include("cards-app-rabbit")
include("cards-app-common")
include("cards-repo-in-memory")
include("cards-repo-postgresql")
include("cards-repo-tests")

include("placedcards-api-log")
include("placedcards-api-v1-jackson")
include("placedcards-common")
include("placedcards-mappers-log")
include("placedcards-mappers-v1")
include("placedcards-stubs")
include("placedcards-biz")
include("placedcards-app-ktor")
include("placedcards-app-kafka")
include("placedcards-app-rabbit")
include("placedcards-app-common")
include("placedcards-repo-tests")


