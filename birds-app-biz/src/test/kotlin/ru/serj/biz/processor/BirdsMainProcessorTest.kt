package ru.serj.biz.processor

import BirdsContext
import BirdsTweetPermission.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import models.*
import models.BirdsState.FAILED
import org.junit.Test

import org.junit.jupiter.api.Assertions.*
import ru.serj.biz.stubs.tweetResponseStub

internal class BirdsMainProcessorTest {

    @Test
    fun `create request should work`() = runBlocking {
        // given
        val processor = BirdsMainProcessor()
        val ctx = BirdsContext(
            command = BirdsCommand.CREATE,
            state = BirdsState.NONE,
            workMode = BirdsWorkMode.TEST,
            stubCase = BirdsStubs.SUCCESS,
            requestId = BirdsRequestId("birds-request-id"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetRequest = BirdsTweet(
                text = "text",
                containsMedia = false,
                type = BirdsTweetType.ORIGINAL,
                ownerId = BirdsUserId("owner-id"),
                visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
                permissions = mutableListOf(READ, UPDATE),
                version = "1"
            )
        )

        // when
        processor.process(ctx)

        // then
        assertEquals(tweetResponseStub.id, ctx.tweetResponse.id)
        assertEquals(tweetResponseStub.containsMedia, ctx.tweetResponse.containsMedia)
        assertEquals(tweetResponseStub.ownerId, ctx.tweetResponse.ownerId)
        assertEquals(tweetResponseStub.text, ctx.tweetResponse.text)
        assertEquals(tweetResponseStub.type, ctx.tweetResponse.type)
        assertEquals(tweetResponseStub.permissions, ctx.tweetResponse.permissions)
        assertEquals(tweetResponseStub.version, ctx.tweetResponse.version)
        assertEquals(tweetResponseStub.visibility, ctx.tweetResponse.visibility)
    }

    @Test
    fun `large tweet should fail`() = runBlocking {
        // given
        val processor = BirdsMainProcessor()
        val ctx = BirdsContext(
            command = BirdsCommand.CREATE,
            state = BirdsState.NONE,
            workMode = BirdsWorkMode.TEST,
            stubCase = BirdsStubs.SUCCESS,
            requestId = BirdsRequestId("birds-request-id"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetRequest = BirdsTweet(
                text = "text-text-text-text-text-text-text-text-text-text-text-text-text-text-text-" +
                        "text-text-text-text-text-text-text-text-text-text-text-text-text-text-text-" +
                        "text-text-text-text-text-text-text-text-text-text-text-text-text-text-text-" +
                        "text-text-text-text-text-text-text-text-text-text-text-text-text-text-text-",
                containsMedia = false,
                type = BirdsTweetType.ORIGINAL,
                ownerId = BirdsUserId("owner-id"),
                visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
                permissions = mutableListOf(READ, UPDATE),
                version = "1"
            )
        )

        // when
        processor.process(ctx)

        // then
        assertEquals(1, ctx.errors.size)
        assertEquals("bad-tweet-size", ctx.errors[0].code)
        assertEquals(FAILED, ctx.state)
    }

    @Test
    fun `bad description stub`() = runBlocking {
        // given
        val processor = BirdsMainProcessor()
        val ctx = BirdsContext(
            command = BirdsCommand.CREATE,
            state = BirdsState.NONE,
            workMode = BirdsWorkMode.TEST,
            stubCase = BirdsStubs.BAD_DESCRIPTION,
            requestId = BirdsRequestId("birds-request-id"),
            timeStart = Instant.fromEpochSeconds(123456),
            tweetRequest = BirdsTweet(
                text = "text",
                containsMedia = false,
                type = BirdsTweetType.ORIGINAL,
                ownerId = BirdsUserId("owner-id"),
                visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
                permissions = mutableListOf(READ, UPDATE),
                version = "1"
            )
        )

        // when
        processor.process(ctx)

        // then
        assertEquals(1, ctx.errors.size)
        assertEquals("bad-description", ctx.errors[0].code)
        assertEquals(FAILED, ctx.state)
    }
}