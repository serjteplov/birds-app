package ru.serj.biz.stubs

import BirdsContext
import models.*
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubSuccessFilter(name: String) =
    worker {
        title = name

        on {
            state == BirdsState.RUNNING && stubCase == BirdsStubs.SUCCESS }

        handle {
            state = BirdsState.DONE
            tweetMultiResponse = mutableListOf(tweetResponseStub)
        }
    }

