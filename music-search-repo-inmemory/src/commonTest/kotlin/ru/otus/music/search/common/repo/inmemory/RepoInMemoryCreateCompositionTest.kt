package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCompositionCreateTest

class RepoInMemoryCreateCompositionTest : RepoCompositionCreateTest() {
    override val repo = CompositionRepoInMemory(
            initObjects = initObjects,
            randomUuid = { lockNew.asString() }
    )
}