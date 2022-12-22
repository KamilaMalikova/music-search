package ru.otus.music.search.common.models

data class MsComposition (
    var id: MsCompositionId = MsCompositionId.NONE,
    var owner: MsUserId = MsUserId.NONE,
    var file: MsFile = MsFile.NONE,
)