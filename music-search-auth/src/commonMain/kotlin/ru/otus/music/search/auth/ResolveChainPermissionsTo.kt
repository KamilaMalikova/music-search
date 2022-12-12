package ru.otus.music.search.auth

import ru.otus.music.search.common.permissions.MsUserGroups
import ru.otus.music.search.common.permissions.MsUserPermissions

fun resolveChainPermissions(
    groups: Iterable<MsUserGroups>,
) = mutableSetOf<MsUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    MsUserGroups.USER to setOf(
        MsUserPermissions.READ_OWN,
        MsUserPermissions.READ_PUBLIC,
        MsUserPermissions.CREATE_OWN,
        MsUserPermissions.UPDATE_OWN,
        MsUserPermissions.COMMENT_OWN,
        MsUserPermissions.COMMENT_PUBLIC,
        MsUserPermissions.SEARCH_OWN,
        MsUserPermissions.SEARCH_PUBLIC
    ),
    MsUserGroups.MODERATOR_MP to setOf(),
    MsUserGroups.ADMIN_AD to setOf(),
    MsUserGroups.TEST to setOf(),
    MsUserGroups.BAN_AD to setOf(),
)

private val groupPermissionsDenys = mapOf(
    MsUserGroups.USER to setOf(),
    MsUserGroups.MODERATOR_MP to setOf(),
    MsUserGroups.ADMIN_AD to setOf(),
    MsUserGroups.TEST to setOf(),
    MsUserGroups.BAN_AD to setOf(
        MsUserPermissions.CREATE_OWN,
        MsUserPermissions.UPDATE_OWN,
        MsUserPermissions.COMMENT_OWN,
        MsUserPermissions.COMMENT_PUBLIC,
    ),
)
