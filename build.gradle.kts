plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
}

group = "com.camila"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}