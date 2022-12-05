package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import java.util.concurrent.CompletionStage
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.repo.cassandra.model.CommentCassandraDto

class CommentSearchCassandraProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<CommentCassandraDto>
) {
    fun search(filter: MsCompositionId): CompletionStage<Collection<CommentCassandraDto>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter != MsCompositionId.NONE) {
            select = select
                .whereColumn(CommentCassandraDto.COLUMN_COMPOSITION_ID)
                .isEqualTo(QueryBuilder.literal(filter.asString(), context.session.context.codecRegistry))
        }
        val asyncFetcher = AsyncFetcher(entityHelper)

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }
}