import BirdsTweetPermission.*
import kotlinx.datetime.Instant
import models.*
import org.junit.Test
import ru.serj.api.v1.models.*
import kotlin.test.assertEquals

internal class MappersV1ToTransportTest {

    @Test
    fun `toTransport TweetCreateResponse`() {
        // given
        val context = BirdsContext(
            command = BirdsCommand.CREATE,
            state = BirdsState.DONE,
            workMode = BirdsWorkMode.PROD,
            stubCase = BirdsStubs.NONE,
            requestId = BirdsRequestId("requestId"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetResponse = BirdsTweet(
                id = BirdsTweetId("birdsTweetId"),
                text = "text",
                type = BirdsTweetType.ORIGINAL,
                containsMedia = false,
                visibility = BirdsTweetVisibility.TO_GUEST,
                ownerId = BirdsUserId("birdsUserId"),
                permissions = mutableListOf(DELETE_MODERATORS, READ_MODERATORS),
                version = "version1"
            )
        )

        // when
        val response = context.toTransport() as TweetCreateResponse

        // then
        assertEquals(response.id, "birdsTweetId")
        assertEquals(response.ownerId, "birdsUserId")
        assertEquals(response.containsMedia, false)
        assertEquals(response.permissions, setOf(TweetPermissions.DELETE_MODERATE, TweetPermissions.READ_MODERATE))
        assertEquals(response.reply, false)
        assertEquals(response.text, "text")
        assertEquals(response.version, "version1")
        assertEquals(response.visibility, TweetVisibility.TO_GUEST)
        assertEquals(response.requestId, "requestId")
        assertEquals(response.result, ResponseResult.SUCCESS)
    }

    @Test
    fun `toTransport TweetDeleteResponse`() {
        // given
        val context = BirdsContext(
            command = BirdsCommand.DELETE,
            state = BirdsState.DONE,
            workMode = BirdsWorkMode.PROD,
            stubCase = BirdsStubs.NONE,
            requestId = BirdsRequestId("requestId"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetResponse = BirdsTweet(
                id = BirdsTweetId("birdsTweetId"),
                text = "text",
                type = BirdsTweetType.ORIGINAL,
                containsMedia = false,
                visibility = BirdsTweetVisibility.TO_USER,
                ownerId = BirdsUserId("birdsUserId"),
                permissions = mutableListOf(DELETE_USERS, CREATE_USERS, READ_MODERATORS),
            )
        )

        // when
        val response = context.toTransport() as TweetDeleteResponse

        // then
        assertEquals(response.id, "birdsTweetId")
        assertEquals(response.ownerId, "birdsUserId")
        assertEquals(response.requestId, "requestId")
        assertEquals(response.result, ResponseResult.SUCCESS)
    }

    @Test
    fun `toTransport TweetSearchResponse`() {
        // given
        val context = BirdsContext(
            command = BirdsCommand.SEARCH,
            state = BirdsState.DONE,
            workMode = BirdsWorkMode.PROD,
            stubCase = BirdsStubs.NONE,
            requestId = BirdsRequestId("requestId"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetMultiResponse = mutableListOf(
                BirdsTweet(
                    id = BirdsTweetId("birdsTweetId1"),
                    text = "text1",
                    type = BirdsTweetType.ORIGINAL,
                    containsMedia = false,
                    visibility = BirdsTweetVisibility.TO_OWNER,
                    ownerId = BirdsUserId("birdsUserId1"),
                    permissions = mutableListOf(UPDATE_USERS, UPDATE_MODERATORS),
                    version = "version1"
                ),
                BirdsTweet(
                    id = BirdsTweetId("birdsTweetId2"),
                    text = "text2",
                    type = BirdsTweetType.REPLY,
                    containsMedia = true,
                    visibility = BirdsTweetVisibility.TO_GUEST,
                    ownerId = BirdsUserId("birdsUserId2"),
                    permissions = mutableListOf(DELETE_MODERATORS),
                    version = "version2"
                )
            )
        )
        val tsr1 = TweetSingleResponse(
            text = "text1",
            containsMedia = false,
            visibility = TweetVisibility.TO_OWNER,
            reply = false,
            id = "birdsTweetId1",
            ownerId = "birdsUserId1",
            version = "version1",
            permissions = setOf(TweetPermissions.UPDATE_MODERATE, TweetPermissions.UPDATE_OWN)
        )
        val tsr2 = TweetSingleResponse(
            text = "text2",
            containsMedia = true,
            visibility = TweetVisibility.TO_GUEST,
            reply = true,
            id = "birdsTweetId2",
            ownerId = "birdsUserId2",
            version = "version2",
            permissions = setOf(TweetPermissions.DELETE_MODERATE)
        )

        // when
        val response = context.toTransport() as TweetSearchResponse

        // then
        assertEquals(response.requestId, "requestId")
        assertEquals(response.result, ResponseResult.SUCCESS)
        assertEquals(response.tweets, listOf(tsr1, tsr2))
    }

    @Test
    fun `toTransport TweetFilterResponse`() {
        // given
        val context = BirdsContext(
            command = BirdsCommand.FILTER,
            state = BirdsState.DONE,
            workMode = BirdsWorkMode.PROD,
            stubCase = BirdsStubs.NONE,
            requestId = BirdsRequestId("requestId"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetMultiResponse = mutableListOf(
                BirdsTweet(
                    id = BirdsTweetId("birdsTweetId1"),
                    text = "text1",
                    type = BirdsTweetType.ORIGINAL,
                    containsMedia = false,
                    visibility = BirdsTweetVisibility.TO_FOLLOWER,
                    ownerId = BirdsUserId("birdsUserId1"),
                    permissions = mutableListOf(CREATE_USERS, READ_GUESTS),
                    version = "version1"
                ),
                BirdsTweet(
                    id = BirdsTweetId("birdsTweetId2"),
                    text = "text2",
                    type = BirdsTweetType.REPLY,
                    containsMedia = true,
                    visibility = BirdsTweetVisibility.TO_FOLLOWER,
                    ownerId = BirdsUserId("birdsUserId2"),
                    permissions = mutableListOf(DELETE_USERS),
                    version = "version2"
                )
            )
        )
        val tsr1 = TweetSingleResponse(
            text = "text1",
            containsMedia = false,
            visibility = TweetVisibility.TO_FOLLOWER,
            reply = false,
            id = "birdsTweetId1",
            ownerId = "birdsUserId1",
            version = "version1",
            permissions = setOf(TweetPermissions.CREATE_OWN, TweetPermissions.READ_PUBLIC)
        )
        val tsr2 = TweetSingleResponse(
            text = "text2",
            containsMedia = true,
            visibility = TweetVisibility.TO_FOLLOWER,
            reply = true,
            id = "birdsTweetId2",
            ownerId = "birdsUserId2",
            version = "version2",
            permissions = setOf(TweetPermissions.DELETE_OWN)
        )

        // when
        val response = context.toTransport() as TweetFilterResponse

        // then
        assertEquals(response.requestId, "requestId")
        assertEquals(response.result, ResponseResult.SUCCESS)
        assertEquals(response.tweets, listOf(tsr1, tsr2))
    }

    @Test
    fun `toTransport Error`() {
        // given
        val context = BirdsContext(
            command = BirdsCommand.CREATE,
            state = BirdsState.FAILED,
            workMode = BirdsWorkMode.STUB,
            stubCase = BirdsStubs.NONE,
            requestId = BirdsRequestId("requestId"),
            errors = mutableListOf(
                BirdsError(
                    code = "code",
                    group = "group",
                    field = "field",
                    message = "message",
                    exception = RuntimeException()
                )
            )
        )

        // when
        val response = context.toTransport() as TweetCreateResponse

        // then
        assertEquals(response.requestId, "requestId")
        assertEquals(response.result, ResponseResult.ERROR)
        assertEquals(
            response.errors, listOf(
                Error(
                    code = "code",
                    group = "group",
                    field = "field",
                    message = "message"
                )
            )
        )
    }

}