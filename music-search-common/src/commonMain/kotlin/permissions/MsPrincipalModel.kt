package ru.otus.music.search.common.permissions

import ru.otus.music.search.common.models.MsUserId

data class MsPrincipalModel (
    val id: MsUserId = MsUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<MsUserGroups> = emptySet()
) {
    companion object {
        val NONE = MsPrincipalModel()
    }
}