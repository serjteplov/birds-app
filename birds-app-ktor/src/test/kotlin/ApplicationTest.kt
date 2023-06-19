import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.serj.api.v1.models.*
import ru.serj.api.v1.models.TweetRequestDebugMode.TEST
import ru.serj.birds.module
import java.util.*
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

class ApplicationTest {

    val logger: Logger = LoggerFactory.getLogger("testHello")

    @Test
    fun `create bird message`() = testApplication {

        application {
            module()
        }
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("809ad85a-87a7-4d5a-8768-d78ae79fbeb9")
        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/create") {
            contentType(ContentType.Application.Json)
            setBody(
                TweetCreateRequest(
                    requestType = "create",
                    requestId = "requestId",
                    debug = TweetDebug(mode = TEST),
                    tweet = TweetCreateObject(
                        text = "Cool birds tweet"
                    )
                )
            )
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
        assertEquals("809ad85a-87a7-4d5a-8768-d78ae79fbeb9", res.id)
        assertEquals("", res.ownerId)
        assertEquals("", res.version)
        assertEquals(null, res.permissions)
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
                    debug = TweetDebug(mode = TEST),
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
        assertEquals("idToDelete", res.id)
        assertEquals("", res.ownerId)
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
                    debug = TweetDebug(mode = TEST),
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
        assertEquals(emptyList(), res.tweets)
        assertEquals(null, res.errors)
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
                    debug = TweetDebug(mode = TEST),
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
        assertEquals(emptyList(), res.tweets)
        assertEquals(null, res.errors)
    }

    private fun ApplicationTestBuilder.createTestClient() = createClient {
        this.install(ClientNegotiation) {
            jackson {
                setConfig(mapper.serializationConfig)
                setConfig(mapper.deserializationConfig)
            }
        }
    }
}
