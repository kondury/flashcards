rootProject.name = "flashcards"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("m1-init")
include("flashcards-acceptance")
//include("flashcards-api-v1-jackson")
//include("flashcards-common")
//include("flashcards-mappers-v1")

include("cards-api-v1-jackson")
include("cards-common")
include("cards-mappers-v1")
include("placedcards-api-v1-jackson")
include("placedcards-common")
include("placedcards-mappers-v1")

