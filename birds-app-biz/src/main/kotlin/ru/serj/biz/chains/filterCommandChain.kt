package ru.serj.biz.chains

import BirdsContext
import models.BirdsCommand.FILTER
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.filter(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) = chain {
    title = name
    description = "Подцепочка для обработки входящих запросов на фильтрацию"
    on { state == RUNNING && command == FILTER }
    block()
}