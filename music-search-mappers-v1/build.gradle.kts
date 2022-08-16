plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":music-search-api-v1-jackson"))
    implementation(project(":music-search-common"))

    testImplementation(kotlin("test-junit"))
}
