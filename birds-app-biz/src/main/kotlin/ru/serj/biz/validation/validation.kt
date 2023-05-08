package ru.serj.biz.validation

import BirdsContext
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.validation(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) =
    chain {
        title = name
        description = "Прохождение валидации"
        on { state == RUNNING }
        block()
    }