package ru.serj.postgres.repository

import BirdsTweetPermission
import BirdsTweetPermission.READ
import BirdsTweetPermission.UPDATE
import BirdsTweetType
import BirdsTweetType.ORIGINAL
import BirdsTweetType.REPLY
import BirdsTweetVisibility
import BirdsTweetVisibility.VISIBLE_TO_PUBLIC
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import models.BirdsTweet
import models.BirdsTweetId
import models.BirdsUserId
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import org.testcontainers.containers.PostgreSQLContainer
import ru.serj.domain.entity.DbRequest
import ru.serj.domain.entity.SqlProperties
import ru.serj.postgres.entity.Tweets
import java.time.Duration
import java.util.*

import kotlin.test.assertEquals

internal class BirdsPostgresRepositoryTest {

    init {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    private val repository: BirdsPostgresRepository = BirdsPostgresRepository(
        SqlProperties(
            url = postgresSQLProcess.jdbcUrl,
            user = POSTGRES_USER,
            password = POSTGRES_PASSWORD,
            driver = POSTGRES_DRIVER
        )
    )

    @Test
    fun createBirdsTweet() {

        // given
        val tweet = BirdsTweet(
            text = "gremlin",
            containsMedia = false,
            type = ORIGINAL,
            ownerId = BirdsUserId("owner-id"),
            visibility = VISIBLE_TO_PUBLIC,
            permissions = mutableListOf(READ, UPDATE),
            version = "1",
            createdAt = Instant.fromEpochSeconds(1234560)
        )

        // when
        val response = transaction {
            SchemaUtils.create(Tweets)
            repository.createBirdsTweet(DbRequest(tweet = tweet))
        }

        // then
        val birdsTweet = transaction {
            TransactionManager.current().exec("SELECT * FROM tweets2 WHERE id='${response.id.asString()}'") {
                it.next()
                BirdsTweet(
                    id = BirdsTweetId(it.getString("id")),
                    text = it.getString("text"),
                    containsMedia = it.getBoolean("contains_media"),
                    type = if (it.getBoolean("reply")) BirdsTweetType.REPLY else ORIGINAL,
                    ownerId = BirdsUserId(it.getString("owner_id")),
                    visibility = BirdsTweetVisibility.valueOf(it.getString("visibility")),
                    permissions = mutableListOf(BirdsTweetPermission.valueOf(it.getString("permission"))),
                    version = it.getString("version"),
                    createdAt = it.getTimestamp("created_at").toInstant().toKotlinInstant()
                )
            }
        }

        assertEquals("gremlin", birdsTweet?.text)
        assertEquals(false, birdsTweet?.containsMedia)
        assertEquals(ORIGINAL, birdsTweet?.type)
        assertEquals(BirdsUserId("owner-id"), birdsTweet?.ownerId)
        assertEquals(VISIBLE_TO_PUBLIC, birdsTweet?.visibility)
        assertEquals(mutableListOf(READ), birdsTweet?.permissions)
        assertEquals("1", birdsTweet?.version)
        assertEquals(Instant.fromEpochSeconds(1234560), birdsTweet?.createdAt)
    }

    @Test
    fun deleteBirdsTweet() {
        // given
        val tweet = BirdsTweet(
            text = "newsql",
            containsMedia = false,
            type = REPLY,
            ownerId = BirdsUserId("owner-id-alex"),
            visibility = VISIBLE_TO_PUBLIC,
            permissions = mutableListOf(UPDATE),
            version = "1",
            createdAt = Instant.fromEpochSeconds(123456)
        )
        val response = transaction {
            SchemaUtils.create(Tweets)
            repository.createBirdsTweet(DbRequest(tweet = tweet))
        }

        // when
        repository.deleteBirdsTweet(DbRequest(id = BirdsTweetId(response.toString())))

        // then
        val fetchSize = transaction {
            TransactionManager.current().exec("SELECT * FROM tweets2 WHERE id='${response.id.asString()}'") { it.fetchSize }
        }

        assertEquals(0, fetchSize)
    }

    @Test
    fun filterBirdsTweet() {
        // given
        val tweet1 = BirdsTweet(
            text = "green",
            createdAt = Instant.fromEpochSeconds(123456780)
        )
        val tweet2 = BirdsTweet(
            text = "red",
            createdAt = Instant.fromEpochSeconds(123456785)
        )
        val tweet3 = BirdsTweet(
            text = "blue",
            createdAt = Instant.fromEpochSeconds(123456789)
        )
        transaction {
            repository.createBirdsTweet(DbRequest(tweet = tweet1))
            repository.createBirdsTweet(DbRequest(tweet = tweet2))
            repository.createBirdsTweet(DbRequest(tweet = tweet3))
        }

        // when
        val res = transaction {
            repository.filterBirdsTweet(
                DbRequest(
                    from = Instant.fromEpochSeconds(123456770),
                    to = Instant.fromEpochSeconds(123456786)
                )
            )
        }

        // then
        assertEquals(2, res.tweets.count())
        val filtered = res.tweets.filter { it.text in listOf("green", "red") }
        assertEquals(2, filtered.count())
    }

    @Test
    fun searchBirdsTweet() {
        // given
        val tweet1 = BirdsTweet(
            text = "oak",
            createdAt = Instant.fromEpochSeconds(1234567801)
        )
        val tweet2 = BirdsTweet(
            text = "redwood",
            createdAt = Instant.fromEpochSeconds(1234567851)
        )
        val tweet3 = BirdsTweet(
            text = "elm",
            createdAt = Instant.fromEpochSeconds(1234567891)
        )
        transaction {
            repository.createBirdsTweet(DbRequest(tweet = tweet1))
            repository.createBirdsTweet(DbRequest(tweet = tweet2))
            repository.createBirdsTweet(DbRequest(tweet = tweet3))
        }

        // when
        val res = transaction {
            repository.searchBirdsTweet(DbRequest(search = "elm"))
        }

        // then
        assertEquals(1, res.tweets.count())
        assertEquals("elm", res.tweets[0].text)
    }

    companion object {
        const val POSTGRES_USER = "bob"
        const val POSTGRES_PASSWORD = "123"
        const val POSTGRES_DRIVER = "org.postgresql.Driver"

        val postgresSQLProcess: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:13.8-alpine")
                .withUsername("bob")
                .withPassword("123")
                .withDatabaseName("tweets")
                .withStartupTimeout(Duration.ofSeconds(60))
                .withEnv("POSTGRES_HOST_AUTH_METHOD", "trust")
                .apply {
                    listOf(
                        "timezone=UTC",
                        "synchronous_commit=off",
                        "max_connections=300",
                        "fsync=off"
                    ).forEach {
                        setCommand("postgres", "-c", it)
                    }
                    start()
                }
    }
}