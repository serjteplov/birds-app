package models

import BirdsTweetPermission
import BirdsTweetType
import BirdsTweetVisibility
import NONE
import kotlinx.datetime.Instant

data class BirdsTweet(
    var id: BirdsTweetId  = BirdsTweetId.NONE,
    var text: String = "",
    var containsMedia: Boolean = false,
    var type : BirdsTweetType = BirdsTweetType.NONE,
    var ownerId: BirdsUserId = BirdsUserId.NONE,
    var visibility: BirdsTweetVisibility = BirdsTweetVisibility.NONE,
    var permissions: MutableList<BirdsTweetPermission> = mutableListOf(),
    var version: String = "",
    var createdAt: Instant = Instant.NONE
)
