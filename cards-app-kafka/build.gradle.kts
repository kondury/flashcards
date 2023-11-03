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
    val logbackVersion: String by project
    val fluentdLoggerVersion: String by project
    val moreAppendersVersion: String by project
    val jUnitJupiterVersion: String by project

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(":flashcards-app-kafka"))
    implementation(project(":cards-common"))
    implementation(project(":cards-api-v1-jackson"))
    implementation(project(":cards-mappers-v1"))
    implementation(project(":cards-biz"))
    implementation(project(":cards-app-common"))

    implementation(project(":cards-api-log"))
    implementation(project(":cards-mappers-log"))
    implementation(project(":flashcards-lib-logging-common"))
    implementation(project(":flashcards-lib-logging-logback"))

    implementation("com.sndyuk:logback-more-appenders:$moreAppendersVersion")
    implementation("org.fluentd:fluent-logger:$fluentdLoggerVersion")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
