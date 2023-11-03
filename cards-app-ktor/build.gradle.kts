import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val jUnitJupiterVersion: String by project
val fluentdLoggerVersion: String by project
val moreAppendersVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    kotlin("jvm")
    id("application")
    id("io.ktor.plugin")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":cards-api-v1-jackson"))
    implementation(project(":cards-mappers-v1"))
    implementation(project(":cards-common"))
    implementation(project(":cards-biz"))
    implementation(project(":cards-app-common"))

    implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktor("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

    implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
    implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation

//    implementation(ktor("locations"))
//    implementation(ktor("caching-headers"))
//    implementation(ktor("call-logging"))
//    implementation(ktor("auto-head-response"))
//    implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
//    implementation(ktor("default-headers")) // "io.ktor:ktor-cors:$ktorVersion"
//    implementation(ktor("auto-head-response"))
//    implementation(ktor("auth")) // "io.ktor:ktor-auth:$ktorVersion"
//    implementation(ktor("auth-jwt")) // "io.ktor:ktor-auth-jwt:$ktorVersion"

    // logging
    implementation(ktor("call-logging-jvm"))
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":cards-api-log"))
    implementation(project(":cards-mappers-log"))
    implementation(project(":flashcards-lib-logging-common"))
    implementation(project(":flashcards-lib-logging-logback"))

    implementation("com.sndyuk:logback-more-appenders:$moreAppendersVersion")
    implementation("org.fluentd:fluent-logger:$fluentdLoggerVersion")

    implementation("io.ktor:ktor-server-swagger:$ktorVersion")

    testImplementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
    testImplementation(ktor("content-negotiation", prefix = "client-"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
}

ktor {
    docker {
        localImageName.set(project.name + "-ktor")
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
//    container.mainClass = "io.ktor.server.cio.EngineMain"
    container.mainClass = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}