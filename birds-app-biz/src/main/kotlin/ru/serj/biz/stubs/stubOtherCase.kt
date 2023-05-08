package ru.serj.biz.stubs

import BirdsContext
import models.BirdsError
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubOtherCase(name: String) =
    worker {
        title = name
        on { state == RUNNING }
        handle {
            state = models.BirdsState.FAILED
            errors.add(
                BirdsError(
                    code = "bad-stubCase",
                    group = "functional",
                    field = "n/a",
                    message = "incorrect stubCase for operation CREATE",
                )
            )
        }
    }