import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.serj.api.v1.models.*
import ru.serj.api.v1.models.TweetPermissions.*
import ru.serj.api.v1.models.TweetRequestDebugMode.PROD
import ru.serj.api.v1.models.TweetRequestDebugMode.TEST
import ru.serj.birds.moduleBird
import ru.serj.birds.settings.AppSettings
import ru.serj.birds.settings.AuthSettings
import ru.serj.biz.processor.BirdsMainProcessor
import ru.serj.`in`.memory.repository.InMemoryTweetRepository
import settings.BirdsLoggerSettings
import java.util.*
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

class ApplicationTest {

    val logger: Logger = LoggerFactory.getLogger("testHello")
    private val appSettings: AppSettings = AppSettings(
        corSettings = BirdsMainProcessor(InMemoryTweetRepository()),
        logSettings = BirdsLoggerSettings(LogbackLoggingProvider())
    )

    @Test
    fun `create bird message`() = testApplication {

        application {
            moduleBird(appSettings, AuthSettings.TEST)
        }
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("809ad85a-87a7-4d5a-8768-d78ae79fbeb9")
        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/create") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer ${testAccessToken(AuthSettings.TEST)}")
            setBody(
                TweetCreateRequest(
                    requestType = "create",
                    requestId = "requestId",
                    debug = TweetDebug(mode = PROD),
                    tweet = TweetCreateObject(
                        text = "Cool birds tweet",
                        visibility = TweetVisibility.TO_GUEST
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
        assertEquals(TweetVisibility.TO_GUEST, res.visibility)
        assertEquals("809ad85a-87a7-4d5a-8768-d78ae79fbeb9", res.id)
        assertEquals(USER_ID, res.ownerId)
        assertEquals("1", res.version)
        assertEquals(setOf(READ_OWN, CREATE_OWN, UPDATE_OWN, DELETE_OWN), res.permissions)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `delete bird message`() = testApplication {

        application {
            moduleBird(appSettings, AuthSettings.TEST)
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/delete") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer ${testAccessToken(AuthSettings.TEST)}")
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
        assertEquals("", res.id)
        assertEquals("", res.ownerId)
    }

    @Test
    fun `filter bird message`() = testApplication {

        application {
            moduleBird(appSettings, AuthSettings.TEST)
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/filter") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer ${testAccessToken(AuthSettings.TEST)}")
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
            moduleBird(appSettings, AuthSettings.TEST)
        }

        val ktorClient = createTestClient()

        val response = ktorClient.post("/bird/v1/search") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer ${testAccessToken(AuthSettings.TEST)}")
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

    private fun testAccessToken(authSettings: AuthSettings) = JWT.create()
        .withAudience(authSettings.audience)
        .withIssuer(authSettings.issuer)
        .withClaim("groups", listOf("MODERATORS", "USERS"))
        .withClaim("sub", USER_ID)
        .sign(Algorithm.HMAC256(authSettings.secret))
}
