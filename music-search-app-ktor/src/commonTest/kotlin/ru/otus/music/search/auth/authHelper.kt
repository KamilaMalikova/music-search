package ru.otus.music.search.auth

import io.ktor.client.request.HttpRequestBuilder
import ru.otus.music.search.base.KtorAuthConfig

expect fun HttpRequestBuilder.addAuth(
    id: String = "user1",
    groups: List<String> = listOf("USER", "TEST"),
    config: KtorAuthConfig = KtorAuthConfig.TEST,
)