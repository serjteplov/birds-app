package models

import BirdsTweetPermission
import BirdsTweetType
import BirdsTweetVisibility

data class BirdsTweet(
    var id: BirdsTweetId  = BirdsTweetId.NONE,
    var text: String = "",
    var containsMedia: Boolean = false,
    var type : BirdsTweetType = BirdsTweetType.NONE,
    var ownerId: BirdsUserId = BirdsUserId.NONE,
    var visibility: BirdsTweetVisibility = BirdsTweetVisibility.NONE,
    var permissions: MutableList<BirdsTweetPermission> = mutableListOf(),
    var version: String = ""
)
