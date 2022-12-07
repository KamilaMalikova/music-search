package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface CassandraMapper {
    @DaoFactory
    fun compositionDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): CompositionCassandraDao
    @DaoFactory
    fun commentDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): CommentsCassandraDao

    companion object {
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}