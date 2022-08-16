package ru.otus.music.search.common.models

@JvmInline
value class MsCommentId (private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MsCommentId("")
    }
}