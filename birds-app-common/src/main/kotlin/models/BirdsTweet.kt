package models

import NONE
import kotlinx.datetime.Instant

@JvmInline
value class BirdsTweetId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BirdsTweetId("")
    }
}

@JvmInline
value class BirdsUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BirdsUserId("")
    }
}

enum class BirdsTweetType {
    NONE,
    ORIGINAL,
    REPLY
}

enum class BirdsTweetVisibility {
    NONE,
    VISIBLE_TO_OWNER,
    VISIBLE_TO_GROUP,
    VISIBLE_TO_PUBLIC
}
enum class BirdsTweetPermission {
    READ,
    UPDATE,
    DELETE
}

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

data class BirdsTweetSearch(
    var searchString: String = "",
    var type: BirdsTweetType = BirdsTweetType.NONE,
    var ownerId: BirdsUserId = BirdsUserId.NONE,
)

data class BirdsFilterPeriod(
    var from: Instant? = Instant.NONE,
    var to: Instant? = Instant.NONE
)
