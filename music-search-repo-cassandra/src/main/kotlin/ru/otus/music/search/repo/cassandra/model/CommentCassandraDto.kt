package ru.otus.music.search.repo.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsUserId

@Entity
data class CommentCassandraDto(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey(0)
    val id: String? = null,

    @field:CqlName(COLUMN_COMPOSITION_ID)
    val compositionId: String? = null,

    @field:CqlName(COLUMN_AUTHOR)
    val author: String? = null,

    @field:CqlName(COLUMN_TEXT)
    val commentText: String? = null,

    @field:CqlName(COLUMN_STATUS)
    val commentStatus: CommentStatus? = null,

    @field:CqlName(COLUMN_LOCK)
    val lock: String? = null
) {
    constructor(compositionId: MsCompositionId, msComment: MsComment) : this(
        compositionId = compositionId.takeIf { it != MsCompositionId.NONE }?.asString(),
        id = msComment.id.takeIf { it != MsCommentId.NONE }?.asString(),
        author = msComment.author.takeIf { it != MsUserId.NONE }?.asString(),
        commentText = msComment.text.takeIf { it.isNotBlank() },
        commentStatus = msComment.status.toTransport(),
        lock = msComment.lock.takeIf { it != MsCompositionLock.NONE }?.asString()
    )

    fun toModel(): MsComment = MsComment(
        id = id?.let { MsCommentId(it) } ?: MsCommentId.NONE,
        author = author?.let { MsUserId(it) } ?: MsUserId.NONE,
        text = commentText ?: "",
        status = commentStatus.fromTransport(),
        lock = lock?.let { MsCompositionLock(it) } ?: MsCompositionLock.NONE
    )

    companion object {
        const val TABLE_NAME = "comments"
        const val COLUMN_COMPOSITION_ID = "composition_id"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_TEXT = "text"
        const val COLUMN_STATUS = "status"
        const val COLUMN_LOCK = "lock"
        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_COMPOSITION_ID, DataTypes.TEXT)
                .withColumn(COLUMN_AUTHOR, DataTypes.TEXT)
                .withColumn(COLUMN_TEXT, DataTypes.TEXT)
                .withColumn(COLUMN_STATUS, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()

        fun commentIndex(keyspace: String, tableName: String, locale: String = "en") =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_COMPOSITION_ID)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
    }
}