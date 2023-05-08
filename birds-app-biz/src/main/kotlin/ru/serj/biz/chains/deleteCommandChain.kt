package ru.serj.biz.chains

import BirdsContext
import models.BirdsCommand.DELETE
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.delete(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) = chain {
    title = name
    description = "Подцепочка для обработки входящих запросов на удаление"
    on { state == RUNNING && command == DELETE }
    block()
}