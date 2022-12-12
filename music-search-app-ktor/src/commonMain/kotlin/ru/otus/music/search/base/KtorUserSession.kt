package ru.otus.music.search.base

import io.ktor.websocket.WebSocketSession
import ru.otus.music.search.common.models.IClientSession

data class KtorUserSession(
    override val fwSession: WebSocketSession,
    override val apiVersion: String,
): IClientSession<WebSocketSession>
