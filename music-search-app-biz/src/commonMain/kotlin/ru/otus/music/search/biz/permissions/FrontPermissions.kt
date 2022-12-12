package ru.otus.music.search.biz.permissions

import ru.otus.music.search.auth.resolveFrontPermissions
import ru.otus.music.search.auth.resolveRelationsTo
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Checking front user grants"

    on { state == MsState.RUNNING }

    handle {
        msRepoDone.permissionsClient.addAll(
             resolveFrontPermissions(
                permissionsChain,
                // Rechecking relations
                msRepoDone.resolveRelationsTo(principal)
            )
        )

        for (ms in msRepoDones) {
            ms.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    ms.resolveRelationsTo(principal)
                )
            )
        }
    }
}