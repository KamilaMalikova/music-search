package ru.otus.music.search.auth

import io.ktor.client.request.post
import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.otus.music.search.base.KtorAuthConfig
import ru.otus.music.search.module
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            module(authConfig = KtorAuthConfig.TEST)
        }

        val response = client.post("/v1/composition/create") {
            addAuth(config = KtorAuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}