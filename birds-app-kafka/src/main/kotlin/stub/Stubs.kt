package ru.serj.birds.stub

import BirdsTweetPermission.READ_USERS
import BirdsTweetPermission.UPDATE_USERS
import BirdsTweetType
import BirdsTweetVisibility
import models.BirdsTweet
import models.BirdsTweetId
import models.BirdsUserId

val tweetResponseStub = BirdsTweet(
    id = BirdsTweetId("tweetId"),
    text = "Cool birds tweet",
    type = BirdsTweetType.ORIGINAL,
    ownerId = BirdsUserId("userId"),
    visibility = BirdsTweetVisibility.TO_USER,
    permissions = mutableListOf(UPDATE_USERS, READ_USERS)
)