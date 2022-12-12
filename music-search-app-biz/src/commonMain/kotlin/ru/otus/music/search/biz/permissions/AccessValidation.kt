package ru.otus.music.search.biz.permissions

import ru.otus.music.search.auth.checkPermitted
import ru.otus.music.search.auth.resolveRelationsTo
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.fail
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.chain
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Checking grants by principal and grans table"
    on { state == MsState.RUNNING }

    worker("Calculating relation of principal to discussion") {
        msRepoRead.principalRelations = msRepoRead.resolveRelationsTo(principal)
    }
    worker("Checking access rights") {
        permitted = checkPermitted(command, msRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Validating rights"
        description = "Checking rights"
        on { !permitted }
        handle {
            fail(MsError(message = "User is not allowed to perform this operation"))
        }
    }
}
