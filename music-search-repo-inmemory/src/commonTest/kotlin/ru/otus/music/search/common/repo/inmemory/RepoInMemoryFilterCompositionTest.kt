package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCompositionFilterTest

class RepoInMemoryFilterCompositionTest : RepoCompositionFilterTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects
    )
}