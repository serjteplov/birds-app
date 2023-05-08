package ru.serj.biz.validation

import BirdsContext
import models.BirdsError
import models.BirdsState
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.validateSearchStringNotEmpty(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING }
        handle {
            if (tweetSearchRequest.searchString.isEmpty()) {
                state = BirdsState.FAILED
                errors.add(
                    BirdsError(
                        code = "empty-search-string",
                        group = "format",
                        field = "searchString",
                        message = "search string is empty",
                    )
                )
            }
        }
    }