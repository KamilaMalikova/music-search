package ru.otus.music.search.repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import org.junit.Test
import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.api.v1.models.BaseComment
import ru.otus.music.search.api.v1.models.CommentAcceptObject
import ru.otus.music.search.api.v1.models.CommentAcceptRequest
import ru.otus.music.search.api.v1.models.CommentAcceptResponse
import ru.otus.music.search.api.v1.models.CommentAddObject
import ru.otus.music.search.api.v1.models.CommentAddRequest
import ru.otus.music.search.api.v1.models.CommentAddResponse
import ru.otus.music.search.api.v1.models.CommentDeclineObject
import ru.otus.music.search.api.v1.models.CommentDeclineRequest
import ru.otus.music.search.api.v1.models.CommentDeclineResponse
import ru.otus.music.search.api.v1.models.CommentStatus
import ru.otus.music.search.api.v1.models.CompositionCreateObject
import ru.otus.music.search.api.v1.models.CompositionCreateRequest
import ru.otus.music.search.api.v1.models.CompositionCreateResponse
import ru.otus.music.search.api.v1.models.CompositionDebug
import ru.otus.music.search.api.v1.models.CompositionReadObject
import ru.otus.music.search.api.v1.models.CompositionReadRequest
import ru.otus.music.search.api.v1.models.CompositionReadResponse
import ru.otus.music.search.api.v1.models.CompositionRequestDebugMode
import ru.otus.music.search.api.v1.models.CompositionSearchFilter
import ru.otus.music.search.api.v1.models.CompositionSearchRequest
import ru.otus.music.search.api.v1.models.CompositionSearchResponse
import ru.otus.music.search.api.v1.models.DiscussionStatus
import ru.otus.music.search.common.EMPTY_FILE
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.repo.CompositionDbResponse
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.test.CompositionRepositoryMock
import ru.otus.music.search.module
import ru.otus.music.search.stubs.V1StubTest
import java.io.File
import java.util.UUID
import ru.otus.music.search.auth.addAuth
import ru.otus.music.search.base.KtorAuthConfig

class V1CompositionMockApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidSup = "10000000-0000-0000-0000-000000000003"
    private val initComposition = MsCompositionDiscussionStub.prepareResult {
        composition = MsComposition(
            id = MsCompositionId(uuidOld),
            owner = MsUserId("user 1234")
        )
        comment = MsComment(
            id = MsCommentId(uuidSup),
            author = MsUserId("user 123"),
            text = "Comment"
        )
        lock = MsCompositionLock(uuidOld)
    }

    @Test
    fun createComposition() = testApplication {
        val user = "test owner"
        application {
            val repo by lazy {
                CompositionRepositoryMock(
                    invokeCreateComposition = {
                        val composition = it.discussion.composition.copy(id = MsCompositionId(UUID.randomUUID().toString()))
                        CompositionDbResponse(
                            isSuccess = true,
                            data = it.discussion.copy(composition = composition)
                        )
                    }
                )
            }
            val settings by lazy { MsSettings(repoTest = repo) }
            module(settings = settings, authConfig = KtorAuthConfig.TEST)
        }
        val client = myClient()
        val createComposition = CompositionCreateObject(
            file = TEST_FILE,
            owner = "test owner",
            status = DiscussionStatus.OPEN
        )

        val response = client.post("v1/composition/create") {
            val requestObject = CompositionCreateRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                composition = createComposition
            )
            addAuth(id = user, config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CompositionCreateResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertNotNull(responseObject.compositionInfo?.id)
    }

    @Test
    fun readComposition() = testApplication {
        val user = "test user"
        application {
            val repo by lazy {
                CompositionRepositoryMock(
                    invokeReadComposition = {
                        CompositionDbResponse(
                            isSuccess = true,
                            data = initComposition.deepCopy()
                        )
                    }
                )
            }
            val settings by lazy { MsSettings(repoTest = repo) }
            module(settings = settings, authConfig = KtorAuthConfig.TEST)
        }
        val client = myClient()
        val readObject = CompositionReadObject(
            id = uuidOld
        )

        val response = client.post("v1/composition/discussion") {
            val requestObject = CompositionReadRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                composition = readObject
            )
            addAuth(id = user, config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CompositionReadResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(uuidOld, responseObject.compositionInfo?.id)
        assertEquals("user 1234", responseObject.compositionInfo?.composition?.owner)
    }

    @Test
    fun `test add comment`() = testApplication {
        val user = "user 123"
        application {
            val repo by lazy {
                CompositionRepositoryMock(
                    invokeReadComposition = {
                        CompositionDbResponse(
                            isSuccess = true, data = initComposition.deepCopy()
                        )
                    },
                    invokeCreateComment = {
                        CompositionDbResponse(
                            isSuccess = true,
                            data = initComposition.deepCopy()
                        )
                    }
                )
            }
            val settings by lazy { MsSettings(repoTest = repo) }
            module(settings = settings, authConfig = KtorAuthConfig.TEST)
        }

        val client = myClient()
        val commentAddObject = CommentAddObject(
            compositionId = uuidOld,
            comment = BaseComment(
                author = "user 123",
                text = "Comment"
            ),
            lock = uuidOld
        )

        val response = client.post("v1/composition/comment") {
            val requestObject = CommentAddRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                composition = commentAddObject
            )
            addAuth(id = user, config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CommentAddResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(uuidSup, responseObject.commentInfo?.id)
        assertEquals("Comment", responseObject.commentInfo?.comment?.text)
        assertEquals("user 123", responseObject.commentInfo?.comment?.author)
        assertEquals(CommentStatus.NONE, responseObject.commentInfo?.comment?.status)
    }

    @Test
    fun `test accept comment`() = testApplication {
        val user = "user 1234"
        application {
            val repo by lazy {
                CompositionRepositoryMock(
                    invokeReadComposition = {
                        CompositionDbResponse(
                            isSuccess = true, data = initComposition.deepCopy()
                        )
                    },
                    invokeUpdateComment = {
                        val comment = initComposition.comment.copy(status = MsCommentStatus.ACCEPTED)
                        CompositionDbResponse(
                            isSuccess = true,
                            data = initComposition.copy(comment = comment)
                        )
                    },
                    invokeUpdateComposition = {
                        CompositionDbResponse(
                            isSuccess = true,
                            data = initComposition.copy(status = MsDiscussionStatus.CLOSED)
                        )
                    }
                )
            }
            val settings by lazy { MsSettings(repoTest = repo) }
            module(settings = settings, authConfig = KtorAuthConfig.TEST)
        }
        val client = myClient()
        val commentAcceptObject = CommentAcceptObject(
            compositionId = uuidOld,
            commentId = uuidSup,
            lock = uuidOld
        )

        val response = client.post("v1/composition/comment/accept") {
            val requestObject = CommentAcceptRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                composition = commentAcceptObject
            )
            addAuth(id = user, config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CommentAcceptResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(uuidOld, responseObject.compositionInfo?.id)
        assertNotEquals(uuidOld, responseObject.compositionInfo?.lock)
        assertEquals(DiscussionStatus.CLOSED, responseObject.compositionInfo?.composition?.status)
    }

    @Test
    fun `test decline comment`() = testApplication {
        val user = "user 1234"
        application {
            val repo by lazy {
                CompositionRepositoryMock(
                    invokeReadComposition = {
                        CompositionDbResponse(
                            isSuccess = true, data = initComposition.deepCopy()
                        )
                    },
                    invokeUpdateComment = {
                        val comment = initComposition.comment.copy(status = MsCommentStatus.DECLINED)
                        CompositionDbResponse(
                            isSuccess = true,
                            data = initComposition.copy(comment = comment)
                        )
                    },
                    invokeUpdateComposition = {
                        val comment = initComposition.comment.copy(status = MsCommentStatus.DECLINED)
                        CompositionDbResponse(
                            isSuccess = true,
                            data = initComposition.copy(status = MsDiscussionStatus.OPEN, comment = comment)
                        )
                    }
                )
            }
            val settings by lazy { MsSettings(repoTest = repo) }
            module(settings = settings, authConfig = KtorAuthConfig.TEST)
        }
        val client = myClient()
        val commentDeclineObject = CommentDeclineObject(
            compositionId = uuidOld,
            commentId = uuidSup,
            lock = uuidOld
        )

        val response = client.post("v1/composition/comment/decline") {
            val requestObject = CommentDeclineRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                composition = commentDeclineObject
            )
            addAuth(id = user, config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CommentDeclineResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(uuidSup, responseObject.commentInfo?.id)
        assertEquals(CommentStatus.DECLINED, responseObject.commentInfo?.comment?.status)
    }

    @Test
    fun `test search`() = testApplication {
        val user = "user 1234"
        application {
            val repo by lazy {
                CompositionRepositoryMock(
                    invokeFilter = {
                        CompositionFilterDbResponse(
                            isSuccess = true,
                            data = listOf(initComposition)
                        )
                    }
                )
            }
            val settings by lazy { MsSettings(repoTest = repo) }
            module(settings = settings, authConfig = KtorAuthConfig.TEST)
        }
        val client = myClient()
        val filterObject = CompositionSearchFilter(
            discussionStatus = DiscussionStatus.OPEN
        )

        val response = client.post("v1/composition/search") {
            val requestObject = CompositionSearchRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.TEST
                ),
                filter = filterObject
            )
            addAuth(id = user, config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CompositionSearchResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(1, responseObject.compositions?.size)
        assertEquals(DiscussionStatus.OPEN, responseObject.compositions?.firstOrNull()?.composition?.status)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    private companion object {
        val TEST_FILE = V1StubTest::class.java.classLoader.getResource("test_sample.mp3")?.path
            ?.let { File(it) } ?: EMPTY_FILE

        val uuidOld = "10000000-0000-0000-0000-000000000001"
        val uuidSup = "10000000-0000-0000-0000-000000000003"
    }

}