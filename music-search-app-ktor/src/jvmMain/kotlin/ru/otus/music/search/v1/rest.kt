package ru.otus.music.search.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.music.search.biz.MsCompositionProcessor

fun Route.v1Composition(processor: MsCompositionProcessor) {
    route("composition") {
        post("create") {
            call.createComposition(processor)
        }
        post("discussion") {
            call.readCompositionDiscussion(processor)
        }
        post("search") {
            call.searchCompositionDiscussion(processor)
        }
    }
}

fun Route.v1Comment(processor: MsCompositionProcessor) {
    route("composition") {
        route("comment") {
            post {
                call.commentComposition(processor)
            }
            post("accept") {
                call.acceptComment(processor)
            }
            post("decline") {
                call.declineComment(processor)
            }
        }
    }
}