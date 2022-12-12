package ru.otus.music.search.auth

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import java.io.File
import ru.otus.music.search.api.v1.models.CompositionCreateObject
import ru.otus.music.search.api.v1.models.CompositionCreateRequest
import ru.otus.music.search.api.v1.models.CompositionCreateResponse
import ru.otus.music.search.api.v1.models.CompositionDebug
import ru.otus.music.search.api.v1.models.CompositionRequestDebugMode
import ru.otus.music.search.api.v1.models.DiscussionStatus
import ru.otus.music.search.base.KtorAuthConfig
import ru.otus.music.search.common.EMPTY_FILE
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.repo.inmemory.CompositionRepoInMemory
import ru.otus.music.search.module
import kotlin.test.assertEquals

class KeycloakAuthTest {
//    @Test
    fun keycloak() = testApplication {
        val authConfig = KtorAuthConfig.TEST

        val uuidNew = "new"
        val settings = MsSettings(
            repoTest = CompositionRepoInMemory(randomUuid = { uuidNew })
        )
        application {
            module(authConfig = authConfig, settings = settings)
        }

        val client = authClient()

        val createComposition = CompositionCreateObject(
            file = TEST_FILE,
            owner = "otus-test",
            status = DiscussionStatus.OPEN
        )

        val response = client.post("v1/composition/create") {
            val requestObject = CompositionCreateRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                composition = createComposition
            )

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(requestObject)
        }


        val responseObject = response.body<CompositionCreateResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(uuidNew, responseObject.compositionInfo?.id)
    }

    private var bearerToken: BearerTokens? = null
    private val authClient = HttpClient(CIO)

    object KeycloakSettings {
        const val host: String = "http://0.0.0.0:8081"
        val authActionUrl =
            "$host/auth/realms/${KtorAuthConfig.TEST.realm}/protocol/openid-connect/token"
    }

    object UserCred {
        const val user: String = "otus-test"
        const val pass: String = "otus-pass"
    }

    private fun ApplicationTestBuilder.authClient() = createClient {
        install(ContentNegotiation) {
            jackson(ContentType.Application.Json) {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
        install(Auth) {
            bearer {
                loadTokens {
                    bearerToken
                }
                refreshTokens {
                    val resp = authClient.submitForm(
                        url = KeycloakSettings.authActionUrl,
                        formParameters = Parameters.build {
                            append("client_id", KtorAuthConfig.TEST.clientId)
                            if (bearerToken == null) {
                                println(" GOING PASS")
                                append("grant_type", "password")
                                append("username", UserCred.user)
                                append("password", UserCred.pass)
                            } else {
                                println(" GOING TOKEN")
                                append("grant_type", "refresh_token")
                                append("refresh_token", bearerToken?.refreshToken ?: "")
                            }
                        }
                    ) {
                        markAsRefreshTokenRequest()
                    }
                    println(resp.bodyAsText())
                    val om = ObjectMapper()
//                    Какой-то баз, не работает штатное преобразование
//                    val tokenInfo: TokenInfo = resp.body()
                    val tokenInfo: TokenInfo = om.readValue(resp.bodyAsText(), TokenInfo::class.java)
                    bearerToken = BearerTokens(
                        tokenInfo.accessToken ?: "",
                        tokenInfo.refreshToken ?: ""
                    )
                    bearerToken
                }
            }
        }
    }

    private companion object {
        val TEST_FILE = KeycloakAuthTest::class.java.classLoader.getResource("test_sample.mp3")?.path
            ?.let { File(it) } ?: EMPTY_FILE
    }
}