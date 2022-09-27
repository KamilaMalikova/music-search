package ru.otus.music.search.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.music.search.api.v1.models.IRequest
import ru.otus.music.search.api.v1.models.IResponse
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.asMsError
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.mappers.v1.fromTransport
import ru.otus.music.search.mappers.v1.toTransport

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    processor: MsCompositionProcessor,
    command: MsCommand? = null,
) {
    val ctx = MsContext(
        timeStart = Clock.System.now(),
    )
    try {
        val request = receive<Q>()
        ctx.fromTransport(request)
        processor.exec(ctx)
        respond(ctx.toTransport())
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = MsState.FAILING
        ctx.errors.add(e.asMsError())
        processor.exec(ctx)
        respond(ctx.toTransport())
    }
}