package ru.otus.music.search.common.models

data class MsComposition (
    var id: MsCompositionId = MsCompositionId.NONE,
    var fileName: String = "",
    var file: ByteArray = byteArrayOf(),
)