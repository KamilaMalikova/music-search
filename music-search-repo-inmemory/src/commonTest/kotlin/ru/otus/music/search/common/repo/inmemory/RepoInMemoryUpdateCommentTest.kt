package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoUpdateCommentTest

class RepoInMemoryUpdateCommentTest : RepoUpdateCommentTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}