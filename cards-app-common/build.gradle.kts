plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm { }
    linuxX64 { }
    macosX64 { }
    macosArm64 { }

    sourceSets {
        val coroutinesVersion: String by project
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                implementation(project(":cards-common"))
//                implementation(project(":cards-stubs"))
                implementation(project(":cards-biz"))
            }
        }
        val commonTest by getting {
            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
//                implementation(project(":cards-api-v1-jackson"))
//                implementation(project(":cards-mappers-v1"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
        val linuxX64Test by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

