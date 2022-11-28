package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCompositionDeleteTest

class RepoInMemoryDeleteCompositionTest : RepoCompositionDeleteTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects
    )
}