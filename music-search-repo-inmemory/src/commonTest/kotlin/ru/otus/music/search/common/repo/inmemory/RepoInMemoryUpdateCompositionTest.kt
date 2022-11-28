package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCompositionUpdateTest

class RepoInMemoryUpdateCompositionTest : RepoCompositionUpdateTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
