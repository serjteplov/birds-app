package ru.serj.biz.stubs

import BirdsContext
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.stubs(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) =
    chain {
        title = name
        description = "Обработка стабов"
        on { state == RUNNING }
        block()
    }