package ru.otus.music.search.base

import io.ktor.server.auth.jwt.JWTPrincipal
import ru.otus.music.search.base.KtorAuthConfig.Companion.F_NAME_CLAIM
import ru.otus.music.search.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.otus.music.search.base.KtorAuthConfig.Companion.ID_CLAIM
import ru.otus.music.search.base.KtorAuthConfig.Companion.L_NAME_CLAIM
import ru.otus.music.search.base.KtorAuthConfig.Companion.M_NAME_CLAIM
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    MsPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { MsUserId(it) } ?: MsUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> MsUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: MsPrincipalModel.NONE
