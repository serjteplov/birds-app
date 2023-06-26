import Mapper.mapper
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.serj.api.v1.models.*

class SerializationTest {

    @Test
    fun `deserialization search tweet request`() {
        val string = SerializationTest::class.java.getResource("tweetSearchRequest.json")!!.readText()
        val req = TweetSearchRequest(
            requestType = "search",
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.PROD,
                stub = TweetRequestDebugStubs.SUCCESS
            ),
            tweetFilter = TweetSearchText(
                searchString = "searching"
            )
        )
        val resultModel = mapper.readValue(string, TweetSearchRequest::class.java)
        assertEquals("Deserialized wrong!", req, resultModel)
    }

    @Test
    fun `serialization search tweet request`() {
        val string = SerializationTest::class.java.getResource("tweetSearchRequest.json")!!.readText()
        val req = TweetSearchRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.PROD,
                stub = TweetRequestDebugStubs.SUCCESS
            ),
            tweetFilter = TweetSearchText(
                searchString = "searching"
            )
        )
        val resultString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req)
        assertEquals("Serialized wrong!", string, resultString)
    }

    @Test
    fun `deserialization filter tweet response`() {
        val string = SerializationTest::class.java.getResource("tweetFilterResponse.json")!!.readText()
        val expectedModel = TweetFilterResponse(
            requestId = "requestId",
            result = ResponseResult.SUCCESS,
            tweets = listOf(
                TweetSingleResponse(
                    text = "text1",
                    containsMedia = false,
                    reply = false,
                    visibility = TweetVisibility.TO_USER,
                    id = "12345",
                    ownerId = "6789",
                    version = "tag1",
                    permissions = setOf(
                        TweetPermissions.UPDATE_OWN,
                        TweetPermissions.READ_OWN
                    )
                ),
                TweetSingleResponse(
                    text = "text2",
                    containsMedia = true,
                    reply = false,
                    visibility = TweetVisibility.TO_FOLLOWER,
                    id = "12000",
                    ownerId = "6000",
                    version = "tag2",
                    permissions = setOf(
                        TweetPermissions.READ_PUBLIC,
                        TweetPermissions.UPDATE_MODERATE
                    )
                )
            )
        )
        val resultModel = mapper.readValue(string, TweetFilterResponse::class.java)
        assertEquals("Deserialized wrong!", expectedModel, resultModel)
    }

    @Test
    fun `serialization filter tweet response with errors`() {
        val string = SerializationTest::class.java.getResource("tweetFilterResponseError.json")!!.readText()
        val resp = TweetFilterResponse(
            requestId = "requestId",
            result = ResponseResult.ERROR,
            errors = listOf(
                Error(
                    code = "1005",
                    group = "some-error-group",
                    field = "error",
                    message = "Some error occurred!"
                )
            )
        )
        val resultString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp)
        assertEquals("Serialized wrong!", string, resultString)
    }

    @Test
    fun `serialization filter tweet response`() {
        val string = SerializationTest::class.java.getResource("tweetFilterResponse.json")!!.readText()
        val resp = TweetFilterResponse(
            requestId = "requestId",
            result = ResponseResult.SUCCESS,
            tweets = listOf(
                TweetSingleResponse(
                    text = "text1",
                    containsMedia = false,
                    reply = false,
                    visibility = TweetVisibility.TO_USER,
                    id = "12345",
                    ownerId = "6789",
                    version = "tag1",
                    permissions = setOf(
                        TweetPermissions.UPDATE_OWN,
                        TweetPermissions.READ_OWN
                    )
                ),
                TweetSingleResponse(
                    text = "text2",
                    containsMedia = true,
                    reply = false,
                    visibility = TweetVisibility.TO_FOLLOWER,
                    id = "12000",
                    ownerId = "6000",
                    version = "tag2",
                    permissions = setOf(
                        TweetPermissions.READ_PUBLIC,
                        TweetPermissions.UPDATE_MODERATE
                    )
                )
            )
        )
        val resultString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp)
        assertEquals("Serialized wrong!", string, resultString)
    }

    @Test
    fun `deserialization filter tweet request`() {
        val string = SerializationTest::class.java.getResource("tweetFilterRequest.json")!!.readText()
        val req = TweetFilterRequest(
            requestType = "filter",
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.TEST,
                stub = null
            ),
            interval = TweetFilterInterval(
                from = "2023-02-21T04:54:54",
                to = "2023-02-25T01:11:11"
            )
        )
        val resultModel = mapper.readValue(string, TweetFilterRequest::class.java)
        assertEquals("Deserialized wrong!", req, resultModel)
    }

    @Test
    fun `serialization filter tweet request`() {
        val string = SerializationTest::class.java.getResource("tweetFilterRequest.json")!!.readText()
        val req = TweetFilterRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.TEST,
                stub = null
            ),
            interval = TweetFilterInterval(
                from = "2023-02-21T04:54:54",
                to = "2023-02-25T01:11:11"
            )
        )
        val resultString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req)
        assertEquals("Serialized wrong!", string, resultString)
    }

    @Test
    fun `deserialization create tweet request`() {
        val reqExpected = SerializationTest::class.java.getResource("tweetCreateRequest.json")!!.readText()
        val model = TweetCreateRequest(
            requestType = "create",
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.TEST,
                stub = null
            ),
            tweet = TweetCreateObject(
                text = "text",
                containsMedia = false,
                reply = false,
                visibility = TweetVisibility.TO_USER
            )
        )
        val resultModel = mapper.readValue(reqExpected, TweetCreateRequest::class.java)
        assertEquals("Deserialized wrong!", model, resultModel)
    }

    @Test
    fun `serialization create tweet request`() {
        val string = SerializationTest::class.java.getResource("tweetCreateRequest.json")!!.readText()
        val req = TweetCreateRequest(
            requestId = "requestId",
            debug = TweetDebug(
                mode = TweetRequestDebugMode.TEST,
                stub = null
            ),
            tweet = TweetCreateObject(
                text = "text",
                containsMedia = false,
                reply = false,
                visibility = TweetVisibility.TO_USER
            )
        )
        val resultString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req)
        assertEquals("Serialized wrong!", string, resultString)
    }

    @Test
    fun `serialization tweet create object test`() {
        // given
        val tweetCreateObject = SerializationTest::class.java.getResource("tweetCreateObject.json")!!.readText()
        val createdTweet = TweetCreateObject(
            text = "text",
            containsMedia = false,
            reply = false,
            visibility = TweetVisibility.TO_USER
        )

        // when
        val createdTweetString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(createdTweet)

        // then
        assertEquals("Serialized wrong!", tweetCreateObject, createdTweetString)
    }

    @Test
    fun `deserialization tweet create object test`() {
        // given
        val givenJson = SerializationTest::class.java.getResource("tweetCreateObject.json")!!.readText()
        val expectedTweet = TweetCreateObject(
            text = "text",
            containsMedia = false,
            reply = false,
            visibility = TweetVisibility.TO_USER
        )

        // when
        val readValue = mapper.readValue(givenJson, TweetCreateObject::class.java)

        // then
        assertEquals("Deserialization failed.", expectedTweet, readValue)
    }
}
