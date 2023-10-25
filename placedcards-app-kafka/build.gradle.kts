plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("com.github.kondury.flashcards.placedcards.app.kafka.ApplicationKt")
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val jacksonVersion: String by project
    val jUnitJupiterVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    implementation(project(":flashcards-app-kafka"))
    implementation(project(":placedcards-common"))
    implementation(project(":placedcards-api-v1-jackson"))
    implementation(project(":placedcards-mappers-v1"))
    implementation(project(":placedcards-biz"))
    implementation(project(":placedcards-app-common"))

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
