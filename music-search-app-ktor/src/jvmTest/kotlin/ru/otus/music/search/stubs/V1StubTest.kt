package ru.otus.music.search.stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
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
import ru.otus.music.search.api.v1.models.CompositionRequestDebugStubs
import ru.otus.music.search.api.v1.models.CompositionSearchFilter
import ru.otus.music.search.api.v1.models.CompositionSearchRequest
import ru.otus.music.search.api.v1.models.CompositionSearchResponse
import ru.otus.music.search.api.v1.models.DiscussionStatus
import ru.otus.music.search.common.EMPTY_FILE
import java.io.File
import kotlin.test.assertEquals

class V1StubTest {
    @Test
    fun `test create route`() = testApplication {
        val client = myClient()

        val response = client.post("v1/composition/create") {
            val requestObject = CompositionCreateRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.STUB,
                    stub = CompositionRequestDebugStubs.SUCCESS
                ),
                composition = CompositionCreateObject(
                    file = TEST_FILE,
                    owner = "test owner",
                    status = DiscussionStatus.OPEN
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CompositionCreateResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals("1234", responseObject.compositionInfo?.id)
    }

    @Test
    fun `test read composition  route`() = testApplication {
        val client = myClient()

        val response = client.post("v1/composition/discussion") {
            val requestObject = CompositionReadRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.STUB,
                    stub = CompositionRequestDebugStubs.SUCCESS
                ),
                composition = CompositionReadObject(
                    id = "123"
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CompositionReadResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals("1234", responseObject.compositionInfo?.id)
        assertEquals(2, responseObject.comments?.size)
    }

    @Test
    fun `test add comment  route`() = testApplication {
        val client = myClient()

        val response = client.post("v1/composition/comment") {
            val requestObject = CommentAddRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.STUB,
                    stub = CompositionRequestDebugStubs.SUCCESS
                ),
                composition = CommentAddObject(
                    compositionId = "123",
                    comment = BaseComment(
                        author = "1234",
                        text = "Rise of The King",
                        status = CommentStatus.NONE
                    )
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CommentAddResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals("987", responseObject.commentInfo?.id)
        assertEquals("Rise of The King", responseObject.commentInfo?.comment?.text)
        assertEquals("963", responseObject.commentInfo?.comment?.author)
        assertEquals(CommentStatus.NONE, responseObject.commentInfo?.comment?.status)
    }

    @Test
    fun `test accept comment route`() = testApplication {
        val client = myClient()

        val response = client.post("v1/composition/comment/accept") {
            val requestObject = CommentAcceptRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.STUB,
                    stub = CompositionRequestDebugStubs.SUCCESS
                ),
                composition = CommentAcceptObject(
                    compositionId = "123",
                    commentId = "987"
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CommentAcceptResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals("1234", responseObject.compositionInfo?.id)
        assertEquals(2, responseObject.comments?.size)
    }

    @Test
    fun `test decline comment route`() = testApplication {
        val client = myClient()

        val response = client.post("v1/composition/comment/decline") {
            val requestObject = CommentDeclineRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.STUB,
                    stub = CompositionRequestDebugStubs.SUCCESS
                ),
                composition = CommentDeclineObject(
                    compositionId = "123",
                    commentId = "987"
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CommentDeclineResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals("987", responseObject.commentInfo?.id)
    }

    @Test
    fun `test search compositions route`() = testApplication {
        val client = myClient()

        val response = client.post("v1/composition/search") {
            val requestObject = CompositionSearchRequest(
                requestId = "123",
                debug = CompositionDebug(
                    mode = CompositionRequestDebugMode.STUB,
                    stub = CompositionRequestDebugStubs.SUCCESS
                ),
                filter = CompositionSearchFilter(
                    discussionStatus = DiscussionStatus.OPEN,
                    owner = "123"
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObject)
        }

        val responseObject = response.body<CompositionSearchResponse>()

        assertEquals(200, response.status.value)
        assertEquals("123", responseObject.requestId)
        assertEquals(4, responseObject.compositions?.size)

        val firstComposition = responseObject.compositions?.get(0)
        assertEquals("1", firstComposition?.id)
        assertEquals("123", firstComposition?.composition?.owner)
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
    }
}