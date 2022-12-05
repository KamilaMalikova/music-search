package ru.otus.music.search.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures
import java.net.InetSocketAddress
import org.testcontainers.containers.CassandraContainer
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.repo.ICompositionRepository
import ru.otus.music.search.common.repo.test.RepoCompositionCreateTest
import ru.otus.music.search.common.repo.test.RepoCompositionDeleteTest
import ru.otus.music.search.common.repo.test.RepoCompositionFilterTest
import ru.otus.music.search.common.repo.test.RepoCompositionReadTest
import ru.otus.music.search.common.repo.test.RepoCompositionUpdateTest
import ru.otus.music.search.repo.cassandra.model.CommentCassandraDto
import ru.otus.music.search.repo.cassandra.model.CommentStatus
import ru.otus.music.search.repo.cassandra.model.DiscussionCassandraDto
import ru.otus.music.search.repo.cassandra.model.DiscussionStatus

class RepoCassandraCreateTest : RepoCompositionCreateTest() {
    override val repo: ICompositionRepository = TestCompanion.repository(initObjects, "ks_create", lockNew)
}

class RepoCassandraDeleteTest : RepoCompositionDeleteTest() {
    override val repo: ICompositionRepository = TestCompanion.repository(initObjects, "ks_delete", lockOld)
}

class RepoCassandraReadTest : RepoCompositionReadTest() {
    override val repo: ICompositionRepository = TestCompanion.repository(initObjects, "ks_read", MsCompositionLock(""))
}

class RepoCassandraSearchTest : RepoCompositionFilterTest() {
    override val repo: ICompositionRepository = TestCompanion.repository(initObjects, "ks_search", MsCompositionLock(""))
}

class RepoCassandraUpdateTest : RepoCompositionUpdateTest() {
    override val repo: ICompositionRepository = TestCompanion.repository(initObjects, "ks_update", lockNew)
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy { TestCasandraContainer().apply { start() } }

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(CommentStatus::class.java))
            register(EnumNameCodec(DiscussionStatus::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoint(InetSocketAddress(container.host, container.getMappedPort(CassandraContainer.CQL_PORT)))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(container.username, container.password)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(DiscussionCassandraDto.table(keyspace, DiscussionCassandraDto.TABLE_NAME))
        session.execute(CommentCassandraDto.table(keyspace, CommentCassandraDto.TABLE_NAME))
    }

    fun repository(initObjects: List<MsCompositionDiscussion>, keyspace: String, lock: MsCompositionLock): RepoCassandra {
        createSchema(keyspace)
        val compositionDao = mapper.compositionDao(keyspace, DiscussionCassandraDto.TABLE_NAME)
        val commentDao = mapper.commentDao(keyspace, CommentCassandraDto.TABLE_NAME)
        CompletableFutures
            .allDone(initObjects.map { compositionDao.create(DiscussionCassandraDto(it)) })
            .toCompletableFuture()
            .get()

        return RepoCassandra(compositionDao, commentDao, randomUuid = { lock.asString() })
    }
}

