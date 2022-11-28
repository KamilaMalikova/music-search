package ru.otus.music.search.common.repo

import ru.otus.music.search.common.models.MsError

interface IResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<MsError>
}
