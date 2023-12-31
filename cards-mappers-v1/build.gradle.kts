plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val jUnitJupiterVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":cards-api-v1-jackson"))
    implementation(project(":cards-common"))

//    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")

}

tasks.test {
    useJUnitPlatform()
}