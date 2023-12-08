plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosArm64 {}
    macosX64 {}

    sourceSets {
        val cache4kVersion: String by project
        val coroutinesVersion: String by project
        val kmpUUIDVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":placedcards-common"))

                implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("com.benasher44:uuid:$kmpUUIDVersion")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":placedcards-repo-tests"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                val jUnitJupiterVersion: String by project
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
            }

            tasks.withType<Test> {
                useJUnitPlatform()
            }
        }
    }
}
