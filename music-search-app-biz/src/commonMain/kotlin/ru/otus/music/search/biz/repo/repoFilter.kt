package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.CompositionFilterDbRequest
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoFilter(title: String) = worker {
    this.title = title
    description = "Filtering compositions"
    on { state == MsState.RUNNING }
    handle {
        val filter = CompositionFilterDbRequest(
            ownerId = filterValidated.ownerId,
            status = when (filterValidated.discussionStatus) {
                MsDiscussionStatus.OPEN -> MsDiscussionStatus.OPEN
                MsDiscussionStatus.CLOSED -> MsDiscussionStatus.CLOSED
                MsDiscussionStatus.NONE -> MsDiscussionStatus.NONE
            }
        )
        val dbResponse =  repository.filter(filter)

        val resultCompositions = dbResponse.data
        when {
            !resultCompositions.isNullOrEmpty() -> msRepoDones = resultCompositions.toMutableList()
            dbResponse.isSuccess -> return@handle
            else -> {
                state = MsState.FAILING
                errors.addAll(dbResponse.errors)
            }
        }
    }
}