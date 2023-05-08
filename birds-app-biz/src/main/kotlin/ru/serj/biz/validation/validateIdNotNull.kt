package ru.serj.biz.validation

import BirdsContext
import models.BirdsError
import models.BirdsState
import models.BirdsTweetId
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.validateDeleteIdNotNull(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING }
        handle {
            if (tweetRequest.id == BirdsTweetId.NONE) {
                state = BirdsState.FAILED
                errors.add(
                    BirdsError(
                        code = "bad-deletion-id",
                        group = "format",
                        field = "tweetRequest.id",
                        message = "deletion id is empty",
                    )
                )
            }
        }
    }