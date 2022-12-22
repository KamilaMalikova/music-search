package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.Update
import java.util.concurrent.CompletionStage
import ru.otus.music.search.common.repo.CommentsFilterDbRequest
import ru.otus.music.search.repo.cassandra.model.CommentCassandraDto
@Dao
interface CommentsCassandraDao {
    @Insert
    fun create(dto: CommentCassandraDto): CompletionStage<Unit>

    @Select(customWhereClause = "id = :id")
    fun read(id: String): CompletionStage<CommentCassandraDto?>

    @Update(customIfClause = "lock = :prevLock")
    fun update(dto: CommentCassandraDto, prevLock: String): CompletionStage<Boolean>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock =:prevLock", entityClass = [CommentCassandraDto::class])
    fun delete(compositionId: String, id: String, prevLock: String): CompletionStage<Boolean>

    @QueryProvider(providerClass = CommentSearchCassandraProvider::class, entityHelpers = [CommentCassandraDto::class])
    fun search(filter: CommentsFilterDbRequest): CompletionStage<Collection<CommentCassandraDto>>
}