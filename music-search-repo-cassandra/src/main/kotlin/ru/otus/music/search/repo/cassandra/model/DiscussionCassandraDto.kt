package ru.otus.music.search.repo.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import java.io.File
import ru.otus.music.search.common.EMPTY_FILE
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId

@Entity
data class DiscussionCassandraDto(
    @field:PartitionKey
    @field:CqlName(COLUMN_ID)
    val id: String? = null,
    @field:CqlName(COLUMN_OWNER)
    val owner: String? = null,
    @field:CqlName(COLUMN_FILE_NAME)
    val fileName: String? = null,
    @field:CqlName(COLUMN_STATUS)
    val status: DiscussionStatus? = null,
    @field:CqlName(COLUMN_LOCK)
    val lock: String? = null
) {
    constructor(discussion: MsCompositionDiscussion): this(
        id = discussion.composition.id.takeIf { it != MsCompositionId.NONE }?.asString(),
        owner = discussion.composition.owner.takeIf { it != MsUserId.NONE }?.asString(),
        fileName = discussion.composition.file.absolutePath,
        status = discussion.status.toTransport(),
        lock = discussion.lock.takeIf { it != MsCompositionLock.NONE }?.asString()
    )

    fun toModel() = MsCompositionDiscussion(
        composition = MsComposition(
            id = id?.let { MsCompositionId(it) } ?: MsCompositionId.NONE,
            owner = owner?.let { MsUserId(it) } ?: MsUserId.NONE,
            file = fileName?.let { File(it) } ?: EMPTY_FILE
        ),
        status = status.fromTransport(),
        lock = lock?.let { MsCompositionLock(it) } ?: MsCompositionLock.NONE
    )

    fun table(keyspace: String, tableName: String) =
        SchemaBuilder
            .createTable(keyspace, tableName)
            .ifNotExists()
            .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
            .withColumn(COLUMN_OWNER, DataTypes.TEXT)
            .withColumn(COLUMN_FILE_NAME, DataTypes.TEXT)
            .withColumn(COLUMN_STATUS, DataTypes.TEXT)
            .withColumn(COLUMN_LOCK, DataTypes.TEXT)
            .build()

    companion object {
        const val TABLE_NAME = "composition_discussions"

        const val COLUMN_ID = "id"
        const val COLUMN_OWNER = "author"
        const val COLUMN_FILE_NAME = "text"
        const val COLUMN_STATUS = "status"
        const val COLUMN_LOCK = "lock"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_OWNER, DataTypes.TEXT)
                .withColumn(COLUMN_FILE_NAME, DataTypes.TEXT)
                .withColumn(COLUMN_STATUS, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()
    }
}
