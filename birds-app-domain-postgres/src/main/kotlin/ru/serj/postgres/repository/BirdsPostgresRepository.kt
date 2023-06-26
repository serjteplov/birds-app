package ru.serj.postgres.repository

import BirdsTweetType
import kotlinx.datetime.toJavaInstant
import models.BirdsTweetId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.serj.domain.entity.DbError
import ru.serj.domain.entity.DbRequest
import ru.serj.domain.entity.DbResponse
import ru.serj.domain.entity.SqlProperties
import ru.serj.domain.repository.BirdsTweetRepository
import ru.serj.postgres.entity.BirdsTweetEntity
import ru.serj.postgres.entity.Tweets
import ru.serj.postgres.mapping.toBirdsTweet
import java.sql.Timestamp
import java.time.ZoneOffset
import java.util.*
import java.time.LocalDateTime as JLocalDateTime

class BirdsPostgresRepository(
    sqlProperties: SqlProperties
) : BirdsTweetRepository {

    init {
        Database.connect(
            url = sqlProperties.url,
            driver = sqlProperties.driver,
            user = sqlProperties.user,
            password = sqlProperties.password
        )
    }

    override fun createBirdsTweet(request: DbRequest) =
        wrapped(request) { req ->
            SchemaUtils.create(Tweets)
            val id = Tweets.insertAndGetId {
                it[text] = req.tweet.text
                it[containsMedia] = req.tweet.containsMedia
                it[reply] = req.tweet.type == BirdsTweetType.REPLY
                it[ownerId] = req.tweet.ownerId.asString()
                it[visibility] = req.tweet.visibility
                it[version] = req.tweet.version
                it[createdAt] = JLocalDateTime.ofInstant(req.tweet.createdAt.toJavaInstant(), ZoneOffset.UTC)
            }
            DbResponse(id = BirdsTweetId(id.toString()))
        }

    // TODO сделать идемпотентным
    override fun deleteBirdsTweet(request: DbRequest) =
        wrapped(request) { req ->
            Tweets.deleteWhere { Tweets.id eq UUID.fromString(req.id.asString()) }
            DbResponse(id = req.id)
        }

    override fun filterBirdsTweet(request: DbRequest) =
        wrapped(request) {
            val op = Op.build {
                Tweets.createdAt greaterEq Timestamp.from(it.from.toJavaInstant()) and (
                        Tweets.createdAt lessEq Timestamp.from(it.to.toJavaInstant())) and (
                        Tweets.visibility inList request.visibilities)
            }
            val tweets = BirdsTweetEntity.find { op }.map { it.toBirdsTweet() }
            DbResponse(tweets = tweets)
        }

    override fun searchBirdsTweet(request: DbRequest) =
        wrapped(request) {
            val condition = Op.build {
                Tweets.text like "%${request.search}%" and (
                        Tweets.visibility inList request.visibilities)
            }
            val tweets = BirdsTweetEntity.find { condition }.map { it.toBirdsTweet() }
            DbResponse(tweets = tweets)
        }

    override fun findById(request: DbRequest) =
        wrapped(request) {
            val op = Op.build {
                Tweets.id eq UUID.fromString(request.id.asString())
            }
            val tweets = BirdsTweetEntity.find { op }.map { it.toBirdsTweet() }
            DbResponse(tweet = tweets.singleOrNull())
        }


    private fun wrapped(request: DbRequest, block: (DbRequest) -> DbResponse): DbResponse {
        return try {
            transaction {
                addLogger(StdOutSqlLogger)
                addLogger(Slf4jSqlDebugLogger)
                block(request)
            }
        } catch (e: Exception) {
            DbResponse(
                id = request.id,
                errors = listOf(
                    DbError(
                        cause = e.localizedMessage,
                        message = e.stackTraceToString()
                    )
                )
            )
        }
    }
}