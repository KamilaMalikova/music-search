package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoDeleteCommentTest

class RepoInMemoryDeleteCommentTest : RepoDeleteCommentTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects
    )
}