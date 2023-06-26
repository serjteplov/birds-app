package ru.serj.`in`.memory.repository

import models.BirdsTweet
import models.BirdsTweetId
import org.ehcache.Cache
import ru.serj.domain.entity.DbError
import ru.serj.domain.entity.DbRequest
import ru.serj.domain.entity.DbResponse
import ru.serj.domain.repository.BirdsTweetRepository
import ru.serj.`in`.memory.config.CacheConfig
import java.util.*

class InMemoryTweetRepository : BirdsTweetRepository {

    private val cache: Cache<String, BirdsTweet> = CacheConfig().cacheInstance

    //  TODO объединить тестирование репозиторииев
    override fun createBirdsTweet(request: DbRequest): DbResponse {
        return try {
            val randomUUID = UUID.randomUUID()
            cache.put(randomUUID.toString(), request.tweet)
            DbResponse(id = BirdsTweetId(randomUUID.toString()))
        } catch (e: Throwable) {
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

    override fun deleteBirdsTweet(request: DbRequest): DbResponse {
        return try {
            cache.remove(request.id.asString()).let { true }
            DbResponse(id = request.id)
        } catch (e: Throwable) {
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

    override fun filterBirdsTweet(request: DbRequest) = DbResponse(
        tweets = cache.filter {
            it.value.createdAt in request.from..request.to
                    && it.value.visibility in request.visibilities
        }.map { it.value }
    )

    override fun searchBirdsTweet(request: DbRequest) = DbResponse(
        tweets = cache.filter {
            it.value.text.contains(request.search)
                    && it.value.visibility in request.visibilities
        }.map { it.value }
    )
}