package ru.otus.music.search.biz.workers.validation

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.fail
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.validateCompositionIdEmpty(title: String) =
    worker {
        this.title = title
        on { msValidating.composition.id != MsCompositionId.NONE }
        handle {
            fail(
                type = "not-empty",
                field = "id"
            )
        }
    }

fun ICorChainDsl<MsContext>.validateCompositionIdNotEmpty(title: String) =
    worker {
        this.title = title
        on { msValidating.composition.id == MsCompositionId.NONE }
        handle {
            fail(
                type = "empty",
                field = "id"
            )
        }
    }