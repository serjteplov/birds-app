package ru.serj.`in`.memory.repository

import BirdsTweetPermission.READ
import BirdsTweetPermission.UPDATE
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.datetime.Instant
import models.BirdsTweet
import models.BirdsTweetId
import models.BirdsUserId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.serj.domain.entity.DbRequest
import java.util.*

internal class InMemoryTweetRepositoryTest {

    private val repository: InMemoryTweetRepository = InMemoryTweetRepository()

    @BeforeEach
    fun setUp() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString(TEST_UUID)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(UUID::class)
    }

    @Test
    fun createBirdsTweet() {
        // when
        val response = repository.createBirdsTweet(TEST_REQUEST1)

        // then
        assertEquals(0, response.errors.size)
        assertEquals(TEST_UUID, response.id.asString())
    }

    @Test
    fun deleteBirdsTweet() {
        // given
        repository.createBirdsTweet(TEST_REQUEST1)
        val fetched1 = repository.searchBirdsTweet(DbRequest(search = "text"))

        // when
        val response = repository.deleteBirdsTweet(DbRequest(id = BirdsTweetId(TEST_UUID)))

        // then
        val fetched2 = repository.searchBirdsTweet(DbRequest(search = "text"))
        assertEquals(0, response.errors.size)
        assertEquals(1, fetched1.tweets.size)
        assertEquals(0, fetched2.tweets.size)
    }

    @Test
    fun filterBirdsTweet() {
        // given
        repository.createBirdsTweet(TEST_REQUEST2)

        // when
        val response = repository.filterBirdsTweet(
            DbRequest(
                from = Instant.fromEpochSeconds(1686901947),
                to = Instant.fromEpochSeconds(1686901967)
            )
        )

        // then
        assertEquals(0, response.errors.size)
        assertEquals(1, response.tweets.size)
        assertEquals("text2", response.tweets[0].text)
    }

    @Test
    fun searchBirdsTweet() {
        // given
        repository.createBirdsTweet(TEST_REQUEST2)

        // when
        val response = repository.searchBirdsTweet(
            DbRequest(
                search = "text2"
            )
        )

        // then
        assertEquals(0, response.errors.size)
        assertEquals(1, response.tweets.size)
        assertEquals("text2", response.tweets[0].text)
    }

    companion object {
        val TEST_UUID = "809ad85a-87a7-4d5a-8768-d78ae79fbeb9"
        val TEST_REQUEST1 = DbRequest(
            tweet = BirdsTweet(
                text = "text",
                containsMedia = false,
                type = BirdsTweetType.ORIGINAL,
                ownerId = BirdsUserId("owner-id"),
                visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
                permissions = mutableListOf(READ, UPDATE),
                version = "1",
                createdAt = Instant.fromEpochSeconds(1686101957)
            )
        )
        val TEST_REQUEST2 = DbRequest(
            tweet = BirdsTweet(
                text = "text2",
                containsMedia = false,
                type = BirdsTweetType.ORIGINAL,
                ownerId = BirdsUserId("owner-id"),
                visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
                permissions = mutableListOf(READ, UPDATE),
                version = "1",
                createdAt = Instant.fromEpochSeconds(1686901957)
            )
        )
    }
}