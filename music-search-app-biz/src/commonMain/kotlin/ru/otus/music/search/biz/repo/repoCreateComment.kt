package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.repo.CommentDbRequest
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoCreateComment(title: String) = worker {
    this.title = title
    description = "Inserting comment into db"
    on { state == MsState.RUNNING }
    handle {
        val request = CommentDbRequest(
            compositionId = msRepoPrepare.composition.id,
            comment = msRepoPrepare.comment,
            lock = msRepoPrepare.lock
        )
        val result = repository.createComment(request)
        val resultComposition = result.data
        if (result.isSuccess && resultComposition != null) {
            msRepoDone = resultComposition
        } else {
            state = MsState.FAILING
            errors.addAll(result.errors)
        }
    }
}