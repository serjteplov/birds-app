package ru.serj.biz.stubs

import BirdsContext
import models.BirdsState
import models.BirdsStubs
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubNotFoundFilter(name: String) =
    worker {
        title = name
        on { state == BirdsState.RUNNING && stubCase == BirdsStubs.BAD_ID }
        handle {
            state = BirdsState.DONE
            tweetMultiResponse = mutableListOf()
        }
    }