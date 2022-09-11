package ru.otus.music.search.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1Composition() {
    route("composition") {
        post("create") {
            call.createComposition()
        }
        post("discussion") {
            call.readCompositionDiscussion()
        }
        post("search") {
            call.searchCompositionDiscussion()
        }
    }
}

fun Route.v1Comment() {
    route("composition") {
        route("comment") {
            post {
                call.commentComposition()
            }
            post("accept") {
                call.acceptComment()
            }
            post("decline") {
                call.declineComment()
            }
        }
    }
}