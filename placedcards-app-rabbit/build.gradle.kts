plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    val testContainersVersion: String by project
    val jUnitJupiterVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation(project(":placedcards-api-v1-jackson"))
    implementation(project(":placedcards-mappers-v1"))
    implementation(project(":placedcards-common"))
    implementation(project(":placedcards-stubs"))
    implementation(project(":placedcards-biz"))

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}