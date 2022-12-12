package ru.otus.music.search.common

import kotlinx.datetime.Instant
import ru.otus.music.search.common.models.*
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsUserPermissions
import ru.otus.music.search.common.repo.ICompositionRepository
import ru.otus.music.search.common.stubs.MsStub

data class MsContext(
    var settings: MsSettings = MsSettings(),
    var repository: ICompositionRepository = ICompositionRepository.NONE,

    var command: MsCommand = MsCommand.NONE,
    var state: MsState = MsState.NONE,
    val errors: MutableList<MsError> = mutableListOf(),

    var workMode: MsWorkMode = MsWorkMode.PROD,
    var stubCase: MsStub = MsStub.NONE,

    var requestId: MsRequestId = MsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var principal: MsPrincipalModel = MsPrincipalModel.NONE,
    val permissionsChain: MutableSet<MsUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var msRequest: MsCompositionDiscussion = MsCompositionDiscussion(),
    var filterRequest: MsFilter = MsFilter(),

    var msValidating: MsCompositionDiscussion = MsCompositionDiscussion(),
    var filterValidating: MsFilter = MsFilter(),

    var msValidated: MsCompositionDiscussion = MsCompositionDiscussion(),
    var filterValidated: MsFilter = MsFilter(),

    var msRepoRead: MsCompositionDiscussion = MsCompositionDiscussion(),
    var msRepoPrepare: MsCompositionDiscussion = MsCompositionDiscussion(),
    var msRepoDone: MsCompositionDiscussion = MsCompositionDiscussion(),
    var msRepoDones: MutableList<MsCompositionDiscussion> = mutableListOf(),

    var compositionResponse: MsCompositionDiscussion = MsCompositionDiscussion(),
    var compositionsResponse: MutableList<MsCompositionDiscussion> = mutableListOf(),
)
