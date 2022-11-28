package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoUpdateComposition(title: String) = worker {
    this.title = title
    on { state == MsState.RUNNING }
    handle {
        val request = CompositionDiscussionDbRequest(msRepoPrepare)
        val result = repository.updateComposition(request)
        val resultComposition = result.data
        if (result.isSuccess && resultComposition != null) {
            msRepoDone = resultComposition
        } else {
            state = MsState.FAILING
            errors.addAll(result.errors)
            msRepoDone
        }
    }
}