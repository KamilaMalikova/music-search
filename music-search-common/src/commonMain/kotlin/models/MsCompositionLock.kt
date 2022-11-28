package ru.otus.music.search.common.models

data class MsCompositionLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MsCompositionLock("")
    }
}