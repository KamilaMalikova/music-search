package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.Update
import java.util.concurrent.CompletionStage
import ru.otus.music.search.common.repo.CompositionFilterDbRequest
import ru.otus.music.search.repo.cassandra.model.DiscussionCassandraDto

@Dao
interface CompositionCassandraDao {
    @Insert
    fun create(dto: DiscussionCassandraDto): CompletionStage<Unit>

    @Select
    fun read(dto: String): CompletionStage<DiscussionCassandraDto?>

    @Update(customIfClause = "lock = :prevLock")
    fun update(dto: DiscussionCassandraDto, prevLock: String): CompletionStage<Boolean>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock =:prevLock", entityClass = [DiscussionCassandraDto::class])
    fun delete(id: String, prevLock: String): CompletionStage<Boolean>

    @QueryProvider(providerClass = DiscussionSearchCassandraProvider::class, entityHelpers = [DiscussionCassandraDto::class])
    fun search(filter: CompositionFilterDbRequest): CompletionStage<Collection<DiscussionCassandraDto>>
}