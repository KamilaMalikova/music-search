package ru.otus.music.search.biz.permissions

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsSearchPermissions
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.permissions.MsUserPermissions
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.chain
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Setting search boundaries"
    on { state == MsState.RUNNING }
    worker("Setting search type") {
        filterValidated.searchPermissions = setOfNotNull(
            MsSearchPermissions.OWN.takeIf { permissionsChain.contains(MsUserPermissions.SEARCH_OWN) },
            MsSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(MsUserPermissions.SEARCH_PUBLIC) },
        ).toMutableSet()
    }
}
