package ru.serj.biz.chains

import BirdsContext
import models.BirdsCommand.SEARCH
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.search(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) = chain {
    title = name
    description = "Подцепочка для обработки входящих запросов на поиск"
    on { state == RUNNING && command == SEARCH }
    block()
}