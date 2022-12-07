package ru.otus.music.search.common.repo

import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsError


data class CompositionFilterDbResponse(
    override val data: List<MsCompositionDiscussion>?,
    override val isSuccess: Boolean,
    override val errors: List<MsError> = emptyList()
): IResponse<List<MsCompositionDiscussion>> {

    companion object {
        val MOCK_SUCCESS_FILTER_EMPTY = CompositionFilterDbResponse(null, true)
        fun success(result: List<MsCompositionDiscussion>) = CompositionFilterDbResponse(result, true)
        fun error(errors: List<MsError>) = CompositionFilterDbResponse(null, false, errors)
        fun error(error: MsError) = CompositionFilterDbResponse(null, false, listOf(error))
    }
}