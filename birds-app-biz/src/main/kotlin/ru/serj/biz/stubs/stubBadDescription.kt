package ru.serj.biz.stubs

import BirdsContext
import models.BirdsError
import models.BirdsState
import models.BirdsStubs
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubBadDescription(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING && stubCase == BirdsStubs.BAD_DESCRIPTION }
        handle {
            state = BirdsState.FAILED
            errors.add(
                BirdsError(
                    code = "bad-description",
                    group = "business",
                    field = "text",
                    message = "error occurred",
                )
            )
        }
    }