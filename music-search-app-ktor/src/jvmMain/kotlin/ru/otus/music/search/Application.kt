package ru.otus.music.search

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.otus.music.search.api.v1.apiV1Mapper
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.repo.inmemory.CompositionRepoInMemory
import ru.otus.music.search.v1.v1Comment
import ru.otus.music.search.v1.v1Composition

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module(settings: MsSettings? = null) {
    install(Routing)

    install(CORS) {
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }

    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }
    install(CallLogging) {
        level = Level.INFO
    }

    @Suppress("OPT_IN_USAGE")
    install(Locations)

    val corSettings by lazy {
        settings ?: MsSettings(
            repoTest = CompositionRepoInMemory()
        )
    }
    val processor = MsCompositionProcessor(corSettings)
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        route("v1") {
            v1Composition(processor)
            v1Comment(processor)
        }
        static("static") {
            resources("static")
        }
    }
}