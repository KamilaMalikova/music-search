package ru.otus.music.search.common.helpers

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.exceptions.RepoConcurrencyException
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState

fun MsContext.addError(vararg error: MsError) = errors.addAll(error)

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

fun errorRepoConcurrency(
    expectedLock: MsCompositionLock,
    actualLock: MsCompositionLock?,
    exception: Exception? = null,
) = MsError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null
) = MsError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    exception = exception,
)