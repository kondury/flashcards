rootProject.name = "flashcards"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings

    val ktorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false
    }
}

//include("m1-init")
include("flashcards-acceptance")
include("flashcards-app-kafka")
include("flashcards-app-rabbit")

include("cards-api-v1-jackson")
include("cards-api-log")
include("cards-common")
include("cards-mappers-v1")
include("cards-stubs")
include("cards-biz")
include("cards-app-ktor")
include("cards-app-kafka")
include("cards-app-rabbit")
include("cards-app-common")

include("placedcards-api-v1-jackson")
include("placedcards-common")
include("placedcards-mappers-v1")
include("placedcards-stubs")
include("placedcards-biz")
include("placedcards-app-ktor")
include("placedcards-app-kafka")
include("placedcards-app-rabbit")
include("placedcards-app-common")


