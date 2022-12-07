package ru.otus.music.search.common.repo

import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsError

data class CommetnsFilterDbResponse(
    override val data: List<MsComment>?,
    override val isSuccess: Boolean,
    override val errors: List<MsError> = emptyList()
): IResponse<List<MsComment>> {

    companion object {
        val MOCK_SUCCESS_FILTER_EMPTY = CommetnsFilterDbResponse(null, true)
        fun success(result: List<MsComment>) = CommetnsFilterDbResponse(result, true)
        fun error(errors: List<MsError>) = CommetnsFilterDbResponse(null, false, errors)
        fun error(error: MsError) = CommetnsFilterDbResponse(null, false, listOf(error))
    }
}