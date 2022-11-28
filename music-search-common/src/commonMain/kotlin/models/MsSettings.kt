package ru.otus.music.search.common.models

import ru.otus.music.search.common.repo.ICompositionRepository

data class MsSettings(
  val repoStub: ICompositionRepository = ICompositionRepository.NONE,
  val repoTest: ICompositionRepository = ICompositionRepository.NONE,
  val repoProd: ICompositionRepository = ICompositionRepository.NONE
)