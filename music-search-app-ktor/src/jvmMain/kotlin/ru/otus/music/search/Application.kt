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
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.otus.music.search.api.v1.apiV1Mapper
import ru.otus.music.search.base.KtorAuthConfig
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.repo.inmemory.CompositionRepoInMemory
import ru.otus.music.search.v1.v1Comment
import ru.otus.music.search.v1.v1Composition

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module(
    settings: MsSettings? = null,
    authConfig: KtorAuthConfig = KtorAuthConfig(environment)
) {
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
            verifier(
                JWT
                    .require(Algorithm.HMAC256(authConfig.secret))
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            )
//            verifier {
//                val token = it.render().replace(it.authScheme, "").trim()
//                val x = JWT.decode(token)
//                val keyId = x.keyId
//
//                val provider = UrlJwkProvider(
//                    URL("http://0.0.0.0:8081/auth/realms/${KtorAuthConfig.TEST.realm}/protocol/openid-connect/certs")
//                )
//                val jwk = provider.get(keyId)
//                val publicKey = jwk.publicKey
//
//                if (publicKey !is RSAPublicKey) {
//                    throw IllegalArgumentException("Key with ID was found in JWK but is not a RSA-key")
//                }
//
//                val algorithm = Algorithm.RSA256(publicKey, null)
//                val jwt = JWT
//                    .require(algorithm)
//                    .build()
//                println(jwt)
//                jwt
//            }
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
            repoTest = CompositionRepoInMemory()
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