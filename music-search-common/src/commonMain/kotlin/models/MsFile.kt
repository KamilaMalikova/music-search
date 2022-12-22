package ru.otus.music.search.common.models

@JvmInline
value class MsFile(private val fileName: String) {
    fun asString() = fileName

    companion object {
        val NONE = MsFile("")
    }
}
