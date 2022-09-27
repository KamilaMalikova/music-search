package ru.otus.music.search.common

import kotlinx.datetime.Instant
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsFilter
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsRequestId
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.stubs.MsStub
import ru.otus.music.search.common.models.MsWorkMode

data class MsContext(
    var command: MsCommand = MsCommand.NONE,
    var state: MsState = MsState.NONE,
    val errors: MutableList<MsError> = mutableListOf(),

    var workMode: MsWorkMode = MsWorkMode.PROD,
    var stubCase: MsStub = MsStub.NONE,

    var requestId: MsRequestId = MsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var msRequest: MsCompositionDiscussion = MsCompositionDiscussion(),
    var filterRequest: MsFilter = MsFilter(),

    var msValidating: MsCompositionDiscussion = MsCompositionDiscussion(),
    var filterValidating: MsFilter = MsFilter(),

    var msValidated: MsCompositionDiscussion = MsCompositionDiscussion(),
    var filterValidated: MsFilter = MsFilter(),

    var compositionResponse: MsCompositionDiscussion = MsCompositionDiscussion(),
    var compositionsResponse: MutableList<MsCompositionDiscussion> = mutableListOf(),
)
