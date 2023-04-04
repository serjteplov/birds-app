import models.BirdsTweet
import models.BirdsTweetId
import models.BirdsUserId


val tweetResponseStub = BirdsTweet(
    id = BirdsTweetId("tweetId"),
    text = "Cool birds tweet",
    type = BirdsTweetType.ORIGINAL,
    ownerId = BirdsUserId("userId"),
    visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
    permissions = mutableListOf(BirdsTweetPermission.UPDATE, BirdsTweetPermission.READ)
)