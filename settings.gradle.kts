rootProject.name = "music-search"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val kotestVersion: String by settings
        val openapiVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatform") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}
include("music-search-api-v1-jackson")
include("music-search-common")
include("music-search-mappers-v1")
include("music-search-app-ktor")
include("music-search-stubs")
include("music-search-cor")
include("music-search-app-biz")
include("music-search-rabbit")
include("music-search-repo-test")
include("music-search-repo-inmemory")
include("music-search-repo-stub")
include("music-search-repo-cassandra")
include("music-search-auth")
