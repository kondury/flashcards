rootProject.name = "flashcards"

//pluginManagement {
//    plugins {
//        val kotlinVersion: String by settings
//        kotlin("jvm") version kotlinVersion
//    }
//}

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        id("io.kotest.multiplatform") version kotestVersion apply false
    }
}

//include("m1-init")
include("flashcards-acceptance")

