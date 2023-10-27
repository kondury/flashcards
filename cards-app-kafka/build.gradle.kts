plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("com.github.kondury.flashcards.cards.app.kafka.ApplicationKt")
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    val jacksonVersion: String by project
    val jUnitJupiterVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
//    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

//    implementation("ch.qos.logback:logback-classic:$logbackVersion")
//    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation(project(":flashcards-app-kafka"))
    implementation(project(":cards-common"))
    implementation(project(":cards-api-v1-jackson"))
    implementation(project(":cards-mappers-v1"))
    implementation(project(":cards-biz"))
    implementation(project(":cards-app-common"))

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
