package ru.serj.biz.stubs

import BirdsContext
import models.BirdsError
import models.BirdsState
import models.BirdsStubs
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubCannotDelete(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING && stubCase == BirdsStubs.CANNOT_DELETE }
        handle {
            state = BirdsState.FAILED
            errors.add(
                BirdsError(
                    code = "tweet-cannot-delete",
                    group = "business",
                    field = "tweet",
                    message = "this tweet cannot be deleted",
                )
            )
        }
    }