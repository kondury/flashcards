plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val rabbitVersion: String by project
    val coroutinesVersion: String by project
    val logbackVersion: String by project
    val fluentdLoggerVersion: String by project
    val moreAppendersVersion: String by project
    val jUnitJupiterVersion: String by project
    val testContainersVersion: String by project
    val kotlinLoggingJvmVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.sndyuk:logback-more-appenders:$moreAppendersVersion")
    implementation("org.fluentd:fluent-logger:$fluentdLoggerVersion")

    implementation(project(":flashcards-app-rabbit"))
    implementation(project(":cards-api-v1-jackson"))
    implementation(project(":cards-mappers-v1"))
    implementation(project(":cards-common"))
    implementation(project(":cards-biz"))
    implementation(project(":cards-app-common"))

    implementation(project(":cards-api-log"))
    implementation(project(":cards-mappers-log"))
    implementation(project(":flashcards-lib-logging-common"))
    implementation(project(":flashcards-lib-logging-logback"))

    testImplementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingJvmVersion")
    testImplementation(kotlin("test-junit5"))
    testImplementation(project(":cards-stubs"))
    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")

}

tasks.withType<Test> {
    useJUnitPlatform()
}