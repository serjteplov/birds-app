package ru.serj.biz.stubs

import BirdsContext
import models.*
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubSuccess(name: String) =
    worker {
        title = name

        on {
            state == BirdsState.RUNNING &&
                    (stubCase == BirdsStubs.SUCCESS || stubCase == BirdsStubs.NONE) }

        handle {
            state = BirdsState.DONE
            tweetResponse = tweetResponseStub
        }
    }

val tweetResponseStub = BirdsTweet(
    id = BirdsTweetId("tweetId"),
    text = "Cool birds tweet",
    type = BirdsTweetType.ORIGINAL,
    ownerId = BirdsUserId("userId"),
    visibility = BirdsTweetVisibility.VISIBLE_TO_PUBLIC,
    permissions = mutableListOf(BirdsTweetPermission.UPDATE, BirdsTweetPermission.READ)
)