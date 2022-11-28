package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.CompositionFilterRequest
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoFilter(title: String) = worker {
    this.title = title
    description = "Filtering compositions"
    on { state == MsState.RUNNING }
    handle {
        val msRequest = msRepoPrepare
        val filter = CompositionFilterRequest(
            ownerId = msRequest.composition.owner,
            status = when (msRequest.status) {
                MsDiscussionStatus.OPEN -> MsDiscussionStatus.OPEN
                MsDiscussionStatus.CLOSED -> MsDiscussionStatus.CLOSED
                MsDiscussionStatus.NONE -> MsDiscussionStatus.NONE
            }
        )
        val dbResponse = if (filter.status == MsDiscussionStatus.NONE) {
            CompositionFilterDbResponse(
                data = null,
                isSuccess = false,
                errors = listOf(
                    MsError(
                        field = "status",
                        message = "Status of discussion must not be empty"
                    )
                )
            )
        } else {
            repository.filter(filter)
        }

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