package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCommentCreateTest

class RepoInMemoryCreateCommentTest : RepoCommentCreateTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}