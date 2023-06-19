package ru.serj.postgres.mapping

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import models.BirdsTweet
import models.BirdsTweetId
import models.BirdsUserId
import ru.serj.postgres.entity.BirdsTweetEntity

fun BirdsTweetEntity.toBirdsTweet() = 
    BirdsTweet(
        id = BirdsTweetId(id.toString()),
        text = text,
        containsMedia = containsMedia,
        type = if (reply) BirdsTweetType.REPLY else BirdsTweetType.ORIGINAL,
        ownerId = BirdsUserId(ownerId),
        visibility = visibility,
        permissions = mutableListOf(permission),
        version = version,
        createdAt = createdAt.toKotlinLocalDateTime().toInstant(TimeZone.UTC)
    )