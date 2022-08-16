package ru.otus.music.search.common.models

@JvmInline
value class MsRequestId(private val id: String) {
    fun asString(): String = id

    companion object {
        val NONE = MsRequestId("")
    }
}