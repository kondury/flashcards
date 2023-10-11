import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
//val serializationVersion: String by project
val logbackVersion: String by project
val jUnitJupiterVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    kotlin("jvm")
    id("application")
    id("io.ktor.plugin")
    // kotlin("plugin.serialization")
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
    implementation(project(":cards-stubs"))
    implementation(project(":cards-biz"))

    implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktor("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

    implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
    implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation(ktor("call-logging-jvm"))

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