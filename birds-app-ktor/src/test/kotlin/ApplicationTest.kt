import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.serj.api.v1.models.*
import ru.serj.api.v1.models.TweetPermissions.READ
import ru.serj.api.v1.models.TweetPermissions.UPDATE
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation


class ApplicationTest {

    val logger: Logger = LoggerFactory.getLogger("testHello")

    @Test
    fun `create bird message`() = testApplication {

        application {
            module()
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/create") {
            contentType(ContentType.Application.Json)
            setBody(TweetCreateRequest(requestType = "create", requestId = "requestId"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        val res: TweetCreateResponse = response.body()
        assertEquals("requestId", res.requestId)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals("Cool birds tweet", res.text)
        assertEquals(false, res.containsMedia)
        assertEquals(false, res.reply)
        assertEquals(TweetVisibility.PUBLIC, res.visibility)
        assertEquals("tweetId", res.id)
        assertEquals("userId", res.ownerId)
        assertEquals("", res.version)
        assertEquals(setOf(UPDATE, READ), res.permissions)
    }

    @Test
    fun `delete bird message`() = testApplication {

        application {
            module()
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/delete") {
            contentType(ContentType.Application.Json)
            setBody(
                TweetDeleteRequest(
                    requestType = "delete",
                    requestId = "requestId",
                    tweetDelete = TweetDeleteObject(
                        id = "idToDelete",
                        version = "5"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        val res: TweetDeleteResponse = response.body()
        assertEquals("requestId", res.requestId)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals("tweetId", res.id)
        assertEquals("userId", res.ownerId)
    }

    @Test
    fun `filter bird message`() = testApplication {

        application {
            module()
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/filter") {
            contentType(ContentType.Application.Json)
            setBody(
                TweetFilterRequest(
                    requestType = "filter",
                    requestId = "requestId",
                    interval = TweetFilterInterval(
                        from = "2023-03-22T04:36:26",
                        to = "2023-03-22T05:36:26"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        val res: TweetFilterResponse = response.body()
        assertEquals("requestId", res.requestId)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals("Cool birds tweet", res.tweets?.get(0)?.text)
        assertEquals(false, res.tweets?.get(0)?.containsMedia)
        assertEquals(false, res.tweets?.get(0)?.reply)
        assertEquals(TweetVisibility.PUBLIC, res.tweets?.get(0)?.visibility)
        assertEquals("tweetId", res.tweets?.get(0)?.id)
        assertEquals("userId", res.tweets?.get(0)?.ownerId)
        assertEquals("", res.tweets?.get(0)?.version)
        assertEquals(setOf(READ, UPDATE), res.tweets?.get(0)?.permissions)
    }

    @Test
    fun `search bird message`() = testApplication {

        application {
            module()
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/search") {
            contentType(ContentType.Application.Json)
            setBody(
                TweetSearchRequest(
                    requestType = "search",
                    requestId = "requestId",
                    tweetFilter = TweetSearchText(
                        searchString = "google"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        val res: TweetSearchResponse = response.body()
        assertEquals("requestId", res.requestId)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals("Cool birds tweet", res.tweets?.get(0)?.text)
        assertEquals(false, res.tweets?.get(0)?.containsMedia)
        assertEquals(false, res.tweets?.get(0)?.reply)
        assertEquals(TweetVisibility.PUBLIC, res.tweets?.get(0)?.visibility)
        assertEquals("tweetId", res.tweets?.get(0)?.id)
        assertEquals("userId", res.tweets?.get(0)?.ownerId)
        assertEquals("", res.tweets?.get(0)?.version)
        assertEquals(setOf(READ, UPDATE), res.tweets?.get(0)?.permissions)
    }

    private fun ApplicationTestBuilder.createTestClient() = createClient {
        this.install(ClientNegotiation) {
            jackson {
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }
}
