package ru.serj.biz.stubs

import BirdsContext
import models.BirdsError
import models.BirdsState
import models.BirdsStubs
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubDeletionNotFound(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING && stubCase == BirdsStubs.NOT_FOUND }
        handle {
            state = BirdsState.FAILED
            errors.add(
                BirdsError(
                    code = "no-such-tweet-to-delete",
                    group = "functional",
                    field = "tweet.id",
                    message = "tweet deletion with such id not found",
                )
            )
        }
    }