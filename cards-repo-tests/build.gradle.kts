plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosArm64 {}
    macosX64 {}

    sourceSets {
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":cards-common"))
                implementation(project(":cards-stubs"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(kotlin("test-junit5"))
                implementation(kotlin("stdlib-jdk8"))
            }
        }
    }
}
