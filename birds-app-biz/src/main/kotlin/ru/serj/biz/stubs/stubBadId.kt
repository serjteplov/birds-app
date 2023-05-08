package ru.serj.biz.stubs

import BirdsContext
import models.BirdsError
import models.BirdsState
import models.BirdsStubs
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubBadId(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING && stubCase == BirdsStubs.BAD_ID }
        handle {
            state = BirdsState.FAILED
            errors.add(
                BirdsError(
                    code = "bad-id",
                    group = "format",
                    field = "id",
                    message = "error occurred",
                )
            )
        }
    }