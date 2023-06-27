import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.datetime.Instant
import models.*
import org.junit.Test
import ru.serj.api.v1.models.*
import java.util.UUID
import kotlin.test.assertEquals

internal class MappersV1FromTransportTest {

    @Test
    fun `fromTransport TweetCreateRequest`() {
        // given
        val context = BirdsContext()
        val request = TweetCreateRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.TEST,
                stub = TweetRequestDebugStubs.SUCCESS
            ),
            tweet = TweetCreateObject(
                text = "text",
                containsMedia = false,
                reply = false,
                visibility = TweetVisibility.TO_GUEST
            )
        )
        val birdsTweet = BirdsTweet(
            text = "text",
            type = BirdsTweetType.ORIGINAL,
            containsMedia = false,
            visibility = BirdsTweetVisibility.TO_GUEST
        )

        // when
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("809ad85a-87a7-4d5a-8768-d78ae79fbeb9")
        context.fromTransport(request)

        // then
        assertEquals(context.command, BirdsCommand.CREATE)
        assertEquals(context.errors, emptyList())
        assertEquals(context.workMode, BirdsWorkMode.TEST)
        assertEquals(context.stubCase, BirdsStubs.SUCCESS)
        assertEquals(context.requestId, BirdsRequestId("requestId"))
        assertEquals(context.tweetRequest.id, BirdsTweetId("809ad85a-87a7-4d5a-8768-d78ae79fbeb9"))
        assertEquals(context.tweetRequest.text, birdsTweet.text)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `fromTransport TweetDeleteRequest`() {
        // given
        val context = BirdsContext()
        val request = TweetDeleteRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.PROD,
                stub = TweetRequestDebugStubs.CANNOT_DELETE
            ),
            tweetDelete = TweetDeleteObject(
                id = "id",
                version = "version"
            )
        )
        val birdsTweet = BirdsTweet(
            id = BirdsTweetId("id")
        )

        // when
        context.fromTransport(request)

        // then
        assertEquals(BirdsCommand.DELETE, context.command)
        assertEquals(emptyList(), context.errors)
        assertEquals(BirdsWorkMode.PROD, context.workMode)
        assertEquals(BirdsStubs.CANNOT_DELETE, context.stubCase)
        assertEquals(BirdsRequestId("requestId"), context.requestId)
        assertEquals(birdsTweet, context.tweetRequest)
    }

    @Test
    fun `fromTransport TweetFilterRequest`() {
        // given
        val context = BirdsContext()
        val request = TweetFilterRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.STUB,
            ),
            interval = TweetFilterInterval(
                from = "2023-02-10T07:16:56",
                to = "2023-03-10T07:16:56"
            )
        )

        // when
        context.fromTransport(request)

        // then
        assertEquals(BirdsCommand.FILTER, context.command)
        assertEquals(emptyList(), context.errors)
        assertEquals(BirdsWorkMode.STUB, context.workMode)
        assertEquals(BirdsStubs.NONE, context.stubCase)
        assertEquals(BirdsRequestId("requestId"), context.requestId)
        assertEquals(1676013416000, context.tweetFilterPeriod.from.toEpochMilliseconds())
        assertEquals(1678432616000, context.tweetFilterPeriod.to.toEpochMilliseconds())
    }

    @Test
    fun `fromTransport TweetSearchRequest`() {
        // given
        val context = BirdsContext()
        val request = TweetSearchRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.STUB,
            ),
            tweetFilter = TweetSearchText("searching")
        )

        // when
        context.fromTransport(request)

        // then
        assertEquals(BirdsCommand.SEARCH, context.command)
        assertEquals(emptyList(), context.errors)
        assertEquals(BirdsWorkMode.STUB, context.workMode)
        assertEquals(BirdsStubs.NONE, context.stubCase)
        assertEquals(BirdsRequestId("requestId"), context.requestId)
        assertEquals("searching", context.tweetSearchRequest.searchString)
    }

    @Test
    fun `fromTransport Errors`() {
        // given
        val context = BirdsContext()
        val request = TweetFilterRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.STUB,
            ),
            interval = TweetFilterInterval(
                from = "2023-fuuu",
                to = "2023-baaar"
            )
        )

        // when
        context.fromTransport(request)

        // then
        assertEquals(BirdsCommand.FILTER, context.command)
        val birdsError = context.errors[0]
        assertEquals("1000", birdsError.code)
        assertEquals("parsing", birdsError.group)
        assertEquals(
            "java.time.format.DateTimeParseException: Text '2023-fuuu' could not be parsed at index 5",
            birdsError.message
        )
        assertEquals("tweetFilterPeriod", birdsError.field)
        assertEquals(BirdsWorkMode.STUB, context.workMode)
        assertEquals(BirdsStubs.NONE, context.stubCase)
        assertEquals(BirdsRequestId("requestId"), context.requestId)
        assertEquals(Instant.NONE, context.tweetFilterPeriod.from)
        assertEquals(Instant.NONE, context.tweetFilterPeriod.to)
    }
}