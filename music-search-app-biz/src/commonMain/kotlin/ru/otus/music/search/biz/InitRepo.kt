package ru.otus.music.search.biz

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.errorAdministration
import ru.otus.music.search.common.helpers.fail
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.permissions.MsUserGroups
import ru.otus.music.search.common.repo.ICompositionRepository
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Defining repository based on workmode        
    """.trimIndent()
    handle {
        repository = when {
            workMode == MsWorkMode.TEST -> settings.repoTest
            workMode == MsWorkMode.STUB -> settings.repoStub
            principal.groups.contains(MsUserGroups.TEST) -> settings.repoTest
            else -> settings.repoProd
        }
        if (workMode != MsWorkMode.STUB && repository == ICompositionRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}