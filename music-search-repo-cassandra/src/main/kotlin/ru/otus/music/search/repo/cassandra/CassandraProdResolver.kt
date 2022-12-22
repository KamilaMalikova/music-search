package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures
import java.net.InetSocketAddress
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.repo.cassandra.model.CommentCassandraDto
import ru.otus.music.search.repo.cassandra.model.CommentStatus
import ru.otus.music.search.repo.cassandra.model.DiscussionCassandraDto
import ru.otus.music.search.repo.cassandra.model.DiscussionStatus

private val host = "localhost"
private val port = 9042

private val codecRegistry by lazy {
    DefaultCodecRegistry("default").apply {
        register(EnumNameCodec(CommentStatus::class.java))
        register(EnumNameCodec(DiscussionStatus::class.java))
    }
}

private val session= CqlSession.builder()
    .addContactPoint(InetSocketAddress(host, port))
    .withLocalDatacenter("datacenter1")
    .withCodecRegistry(codecRegistry)
    .build()

private val mapper by lazy { CassandraMapper.builder(session).build() }


fun createSchema(keyspace: String) {
    session.execute(
        SchemaBuilder
            .createKeyspace(keyspace)
            .ifNotExists()
            .withSimpleStrategy(1)
            .build()
    )
    session.execute(DiscussionCassandraDto.table(keyspace, DiscussionCassandraDto.TABLE_NAME))
    session.execute(CommentCassandraDto.table(keyspace, CommentCassandraDto.TABLE_NAME))
    session.execute(CommentCassandraDto.commentIndex(keyspace, CommentCassandraDto.TABLE_NAME))
}

fun repository(keyspace: String): RepoCassandra {
    val compositionDao = mapper.compositionDao(keyspace, DiscussionCassandraDto.TABLE_NAME)
    val commentDao = mapper.commentDao(keyspace, CommentCassandraDto.TABLE_NAME)

    return RepoCassandra(compositionDao, commentDao)
}
