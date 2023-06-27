package ru.serj.domain.entity

import BirdsTweetVisibility
import NONE
import kotlinx.datetime.Instant
import models.BirdsTweet
import models.BirdsTweetId

data class DbRequest (
    val id: BirdsTweetId = BirdsTweetId.NONE,
    val tweet: BirdsTweet = BirdsTweet(),
    val from: Instant = Instant.NONE,
    val to: Instant = Instant.NONE,
    val search: String = "",
    val visibilities: Set<BirdsTweetVisibility> = emptySet()
)