package ru.otus.music.search.common.models

@JvmInline
value class MsUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MsUserId("")
    }
}
