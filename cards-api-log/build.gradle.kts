import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("multiplatform")
    id("org.openapi.generator")
    kotlin("plugin.serialization")
}

val generatedResourceDir = layout.buildDirectory.dir("generate-resources").get().toString()

kotlin {
    jvm { }
    linuxX64 { }
    macosX64 { }
    macosArm64 { }

    sourceSets {
//        val coroutinesVersion: String by project
        val serializationVersion: String by project

        val commonMain by getting {
            kotlin.srcDirs("${generatedResourceDir}/main/src/commonMain/kotlin")

            dependencies {
                implementation(kotlin("stdlib-common"))
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//            }
//        }
//        val jvmMain by getting {
//            dependencies {
//                implementation(kotlin("stdlib-jdk8"))
//            }
//        }
//        val jvmTest by getting {
//            val jUnitJupiterVersion: String by project
//
//            dependencies {
//                implementation(kotlin("test-junit5"))
//
//                implementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
////                implementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
//                implementation("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
//            }
//        }

//        all {
//            languageSettings.optIn("kotlin.RequiresOptIn")
//        }
    }
}

openApiGenerate {
    val openapiGroup = "${rootProject.group}.cards.api.logs"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
//    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/specs/spec-cards-log.yaml")
    library.set("multiplatform") // Используем библиотеку для KMP

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    configOptions.set(mapOf(
        "dateLibrary" to "string",
        "enumPropertyNaming" to "UPPERCASE",
        "collectionType" to "list"
    ))
}

//tasks {
//    filter { it.name.startsWith("compile") }.forEach {
//        it.dependsOn(this.openApiGenerate)
//    }
//}

tasks {
    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
//        outputDir.set("$generatedResourceDir/main/src/commonMain/kotlin")
        mustRunAfter("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        println("TASK attach $it")
        it.dependsOn(openApiGenerateTask)
    }
}
