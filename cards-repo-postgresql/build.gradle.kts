plugins {
    kotlin("jvm")
}

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val kmpUUIDVersion: String by project
    val testContainersVersion: String by project
    val jUnitJupiterVersion: String by project


    implementation(kotlin("stdlib"))

    implementation(project(":cards-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.benasher44:uuid:$kmpUUIDVersion")


    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation(project(":cards-repo-tests"))

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

