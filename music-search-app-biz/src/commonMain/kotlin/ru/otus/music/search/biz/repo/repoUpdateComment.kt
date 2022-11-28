package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.repo.CommentUpdateDbRequest
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoUpdateComment(title: String) = worker {
    this.title = title
    on { state == MsState.RUNNING }
    handle {
        val request = CommentUpdateDbRequest(
            compositionId = msRepoPrepare.composition.id,
            comment = msRepoPrepare.comment,
            lock = msRepoPrepare.lock
        )
        val result = repository.updateComment(request)
        val resultComposition = result.data
        if (result.isSuccess && resultComposition != null) {
            msRepoDone = resultComposition
            msRepoPrepare = msRepoPrepare.copy(lock = resultComposition.lock, comments = resultComposition.comments)
        } else {
            state = MsState.FAILING
            errors.addAll(result.errors)
            msRepoDone
        }
    }
}