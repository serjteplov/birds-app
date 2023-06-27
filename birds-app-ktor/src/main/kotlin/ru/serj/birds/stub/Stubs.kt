package ru.serj.birds.stub

import models.BirdsTweet
import models.BirdsTweetId
import models.BirdsUserId

val tweetResponseStub = BirdsTweet(
    id = BirdsTweetId("tweetId"),
    text = "Cool birds tweet",
    type = BirdsTweetType.ORIGINAL,
    ownerId = BirdsUserId("userId"),
    visibility = BirdsTweetVisibility.TO_GUEST,
    permissions = mutableListOf(BirdsTweetPermission.UPDATE_USERS, BirdsTweetPermission.READ_USERS)
)