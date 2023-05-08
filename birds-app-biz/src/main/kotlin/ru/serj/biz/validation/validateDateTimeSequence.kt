package ru.serj.biz.validation

import BirdsContext
import models.BirdsError
import models.BirdsState
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.validateDateTimeSequence(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING }
        handle {
            if (tweetFilterPeriod.from > tweetFilterPeriod.to) {
                state = BirdsState.FAILED
                errors.add(
                    BirdsError(
                        code = "bad-filter-period-sequence",
                        group = "logic",
                        field = "tweetFilterPeriod",
                        message = "from should be grater than to",
                    )
                )
            }
        }
    }