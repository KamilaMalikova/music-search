package ru.otus.music.search.biz.workers.validation

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.fail
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.validateOwnerIdNotEmpty(title: String) =
    worker {
        this.title = title
        on { msValidating.composition.owner == MsUserId.NONE }
        handle {
            fail(
                type = "empty",
                field = "owner"
            )
        }
    }