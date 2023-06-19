package ru.serj.`in`.memory.config

import models.BirdsTweet
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder


class CacheConfig {
    private val cacheManager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder().build()
    private val cache: Cache<String, BirdsTweet>

    init {
        cacheManager.init()
        cache = cacheManager.createCache(
            "birdsTweet", CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                    String::class.java,
                    BirdsTweet::class.java,
                    ResourcePoolsBuilder.heap(10)
                )
        )
    }

    val cacheInstance: Cache<String, BirdsTweet>
        get() = cacheManager.getCache("birdsTweet", String::class.java, BirdsTweet::class.java)

}
