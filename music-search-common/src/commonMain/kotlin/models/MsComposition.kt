package ru.otus.music.search.common.models

import ru.otus.music.search.common.EMPTY_FILE
import java.io.File

data class MsComposition (
    var id: MsCompositionId = MsCompositionId.NONE,
    var file: File = EMPTY_FILE,
)