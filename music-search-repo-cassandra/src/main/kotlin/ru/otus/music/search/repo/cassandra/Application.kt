package ru.otus.music.search.repo.cassandra



fun main() {
    createSchema("music")
    repository("music")
    println("Rope build")
}