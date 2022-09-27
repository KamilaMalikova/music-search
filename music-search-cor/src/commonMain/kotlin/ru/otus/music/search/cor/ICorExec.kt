package ru.otus.music.search.cor

interface ICorExec<T> {
    val title: String
    val description: String

    suspend fun exec(context: T)
}