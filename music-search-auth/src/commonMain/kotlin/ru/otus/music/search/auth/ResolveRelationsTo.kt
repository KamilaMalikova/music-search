package ru.otus.music.search.auth

import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsPrincipalRelations

fun MsCompositionDiscussion.resolveRelationsTo(principal: MsPrincipalModel): Set<MsPrincipalRelations> = setOfNotNull(
    MsPrincipalRelations.NONE,
    MsPrincipalRelations.NEW.takeIf { composition.id == MsCompositionId.NONE },
    MsPrincipalRelations.OWN.takeIf { principal.id == composition.owner },
    MsPrincipalRelations.PUBLIC.takeIf { principal.id != composition.owner }
)
