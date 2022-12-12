package ru.otus.music.search.auth

import ru.otus.music.search.common.permissions.MsPermissionClient
import ru.otus.music.search.common.permissions.MsPrincipalRelations
import ru.otus.music.search.common.permissions.MsUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<MsUserPermissions>,
    relations: Iterable<MsPrincipalRelations>,
) = mutableSetOf<MsPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    MsUserPermissions.READ_OWN to mapOf(
        MsPrincipalRelations.OWN to MsPermissionClient.READ
    ),
    MsUserPermissions.READ_PUBLIC to mapOf(
        MsPrincipalRelations.PUBLIC to MsPermissionClient.READ
    ),

    // UPDATE
    MsUserPermissions.UPDATE_OWN to mapOf(
        MsPrincipalRelations.OWN to MsPermissionClient.UPDATE
    ),

    MsUserPermissions.COMMENT_OWN to mapOf(
        MsPrincipalRelations.OWN to MsPermissionClient.ADD_COMMENT
    ),
    MsUserPermissions.COMMENT_PUBLIC to mapOf(
        MsPrincipalRelations.PUBLIC to MsPermissionClient.ADD_COMMENT
    )
)
