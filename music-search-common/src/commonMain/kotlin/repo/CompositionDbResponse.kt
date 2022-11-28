package ru.otus.music.search.common.repo

import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsError

data class CompositionDbResponse(
    override val data: MsCompositionDiscussion?,
    override val isSuccess: Boolean,
    override val errors: List<MsError> = emptyList()
): IResponse<MsCompositionDiscussion> {

    companion object {
        val MOCK_SUCCESS_EMPTY = CompositionDbResponse(null, true)
        val MOCK_SUCCESS_FILTER_EMPTY = CompositionFilterDbResponse(null, true)
        fun success(result: MsCompositionDiscussion) = CompositionDbResponse(result, true)
        fun error(errors: List<MsError>) = CompositionDbResponse(null, false, errors)
        fun error(error: MsError) = CompositionDbResponse(null, false, listOf(error))
    }
}
