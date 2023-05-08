package ru.serj.biz.validation

import BirdsContext
import models.BirdsError
import models.BirdsState
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.validateTweetSize(name: String) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING
        }
        handle {
            if (tweetRequest.text.isBlank() || tweetRequest.text.length > 240) {
                state = BirdsState.FAILED
                errors.add(
                    BirdsError(
                        code = "bad-tweet-size",
                        group = "format",
                        field = "text",
                        message = "size of tweet out of bounds",
                    )
                )
            }
        }
    }