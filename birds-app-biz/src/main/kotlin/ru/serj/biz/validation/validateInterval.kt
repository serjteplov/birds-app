package ru.serj.biz.validation

import BirdsContext
import INSTANT_NONE
import models.BirdsError
import models.BirdsState
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.validateInterval(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING }
        handle {
            if (tweetFilterPeriod.from == INSTANT_NONE || tweetFilterPeriod.to == INSTANT_NONE) {
                state = BirdsState.FAILED
                errors.add(
                    BirdsError(
                        code = "bad-filter-period",
                        group = "format",
                        field = "tweetFilterPeriod",
                        message = "period has incorrect value",
                    )
                )
            }
        }
    }