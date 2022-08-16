package ru.otus.music.search.common.models

@JvmInline
value class MsCompositionId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MsCompositionId("")
    }
}
