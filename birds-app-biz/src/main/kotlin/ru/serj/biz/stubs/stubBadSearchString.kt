package ru.serj.biz.stubs

import BirdsContext
import models.BirdsError
import models.BirdsState
import models.BirdsStubs
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubBadSearchString(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING && stubCase == BirdsStubs.BAD_SEARCH_STRING }
        handle {
            state = BirdsState.FAILED
            errors.add(
                BirdsError(
                    code = "bad-search-string",
                    group = "business",
                    field = "searchString",
                    message = "search string is incorrect",
                )
            )
        }
    }