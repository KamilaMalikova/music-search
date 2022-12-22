package ru.otus.music.search

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.cio.CIO
import io.ktor.server.config.yaml.YamlConfigLoader
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.InetSocketAddress
import org.slf4j.event.Level
import ru.otus.music.search.api.v1.apiV1Mapper
import ru.otus.music.search.base.KtorAuthConfig
import ru.otus.music.search.base.resolveAlgorithm
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.repo.inmemory.CompositionRepoInMemory
import ru.otus.music.search.repo.cassandra.repository
import ru.otus.music.search.v1.v1Comment
import ru.otus.music.search.v1.v1Composition

fun main(args: Array<String>) {
    embeddedServer(CIO, environment = applicationEngineEnvironment {
        val conf = YamlConfigLoader().load("./application.yaml")
            ?: throw RuntimeException("Cannot read application.yaml")
        println(conf)
        config = conf
        connector {
            port =  8080
            host =  "0.0.0.0"
        }
    }).apply {
        start(true)
    }
}

@Suppress("unused") // Referenced in application.conf
fun Application.module(
    settings: MsSettings? = null,
    authConfig: KtorAuthConfig = KtorAuthConfig(environment)
) {
    println("in module")
    install(Routing)

    install(CORS) {
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm
            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(KtorAuthConfig.GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
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
            repoTest = CompositionRepoInMemory(),
            repoProd = repository("music")
        )
    }
    val processor = MsCompositionProcessor(corSettings)
    routing {
        authenticate("auth-jwt") {
            route("v1") {
                v1Composition(processor)
                v1Comment(processor)
            }
        }
        static("static") {
            resources("static")
        }
    }
}