plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosX64 {}
    macosArm64 {}

    sourceSets {
        val coroutinesVersion: String by project

        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":cards-common"))
                implementation(project(":cards-stubs"))
                implementation(project(":flashcards-lib-cor"))
                implementation(project(":cards-auth"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

                implementation(project(":cards-repo-tests"))
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
