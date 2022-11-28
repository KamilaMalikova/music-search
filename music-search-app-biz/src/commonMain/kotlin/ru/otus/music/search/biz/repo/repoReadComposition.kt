package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.repo.CompositionIdDbRequest
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoReadComposition(title: String) = worker {
    this.title = title
    description = "Reading composition from DB"
    on { state == MsState.RUNNING }
    handle {
        val request = CompositionIdDbRequest(msValidated.composition.id)
        val result = repository.readComposition(request)
        val resultComposition = result.data
        if (result.isSuccess && resultComposition != null) {
            msRepoRead = resultComposition.copy(comment = msValidated.comment)
        } else {
            state = MsState.FAILING
            errors.addAll(result.errors)
        }
    }
}