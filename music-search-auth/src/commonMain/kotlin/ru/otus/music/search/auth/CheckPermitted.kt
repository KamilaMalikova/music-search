package ru.otus.music.search.auth

import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.permissions.MsPrincipalRelations
import ru.otus.music.search.common.permissions.MsUserPermissions

fun checkPermitted(
    command: MsCommand,
    relations: Iterable<MsPrincipalRelations>,
    permissions: Iterable<MsUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: MsCommand,
    val permission: MsUserPermissions,
    val relation: MsPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = MsCommand.CREATE,
        permission = MsUserPermissions.CREATE_OWN,
        relation = MsPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = MsCommand.READ,
        permission = MsUserPermissions.READ_OWN,
        relation = MsPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MsCommand.READ,
        permission = MsUserPermissions.READ_PUBLIC,
        relation = MsPrincipalRelations.PUBLIC,
    ) to true,

    // COMMENT
    AccessTableConditions(
        command = MsCommand.COMMENT,
        permission = MsUserPermissions.COMMENT_OWN,
        relation = MsPrincipalRelations.OWN,
    ) to true,

    AccessTableConditions(
        command = MsCommand.COMMENT,
        permission = MsUserPermissions.COMMENT_PUBLIC,
        relation = MsPrincipalRelations.PUBLIC,
    ) to true,

    // ACCEPT
    AccessTableConditions(
        command = MsCommand.ACCEPT,
        permission = MsUserPermissions.UPDATE_OWN,
        relation = MsPrincipalRelations.OWN,
    ) to true,
    // DECLINE
    AccessTableConditions(
        command = MsCommand.DECLINE,
        permission = MsUserPermissions.UPDATE_OWN,
        relation = MsPrincipalRelations.OWN,
    ) to true,
)
