package ru.serj.domain.entity

import models.BirdsTweet
import models.BirdsTweetId

data class DbResponse(
    val id: BirdsTweetId = BirdsTweetId.NONE,
    val tweet: BirdsTweet? = null,
    val tweets: List<BirdsTweet> = emptyList(),
    val errors: List<DbError> = emptyList()
)