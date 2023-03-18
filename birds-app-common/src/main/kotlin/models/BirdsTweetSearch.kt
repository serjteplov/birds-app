package models

import BirdsTweetType

data class BirdsTweetSearch(
    var searchString: String = "",
    var type: BirdsTweetType = BirdsTweetType.NONE,
    var ownerId: BirdsUserId = BirdsUserId.NONE,
)