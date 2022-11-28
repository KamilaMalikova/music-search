package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCommentReadTest

class RepoInMemoryReadCommentTest : RepoCommentReadTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects
    )
}