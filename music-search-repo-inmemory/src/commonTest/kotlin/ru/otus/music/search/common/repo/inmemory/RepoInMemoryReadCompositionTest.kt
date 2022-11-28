package ru.otus.music.search.common.repo.inmemory

import ru.otus.music.search.common.repo.test.RepoCompositionReadTest

class RepoInMemoryReadCompositionTest : RepoCompositionReadTest() {
    override val repo = CompositionRepoInMemory(
        initObjects = initObjects
    )
}