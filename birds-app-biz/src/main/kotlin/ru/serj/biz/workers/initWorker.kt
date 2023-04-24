package ru.serj.biz.workers

import BirdsContext
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.initWorker(name: String) =
    worker {
        title = name
        description = "Инициализирующий воркер"
        on { state == models.BirdsState.NONE }
        handle { state = models.BirdsState.RUNNING }
    }