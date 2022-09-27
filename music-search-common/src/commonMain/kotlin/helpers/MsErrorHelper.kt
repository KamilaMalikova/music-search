package ru.otus.music.search.common.helpers

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState

fun MsContext.addError(error: MsError) = errors.add(error)

fun MsContext.fail(
    type: String,
    field: String
) = fail(
    MsError(
        code = "validation-$type",
        group = "validation",
        field = field
    )
)

fun MsContext.fail(error: MsError) {
    addError(error)
    state = MsState.FAILING
}

fun Throwable.asMsError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MsError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
