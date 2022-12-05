package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import java.util.concurrent.CompletionStage
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.repo.CompositionFilterDbRequest
import ru.otus.music.search.repo.cassandra.model.DiscussionCassandraDto
import ru.otus.music.search.repo.cassandra.model.toTransport

class DiscussionSearchCassandraProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<DiscussionCassandraDto>
) {
    fun search(filter: CompositionFilterDbRequest): CompletionStage<Collection<DiscussionCassandraDto>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.ownerId != MsUserId.NONE) {
            select = select
                .whereColumn(DiscussionCassandraDto.COLUMN_OWNER)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }

        if (filter.status != MsDiscussionStatus.NONE) {
            select = select
                .whereColumn(DiscussionCassandraDto.COLUMN_STATUS)
            .isEqualTo(QueryBuilder.literal(filter.status.toTransport(), context.session.context.codecRegistry))
        }
        val asyncFetcher = AsyncFetcher(entityHelper)

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }
}