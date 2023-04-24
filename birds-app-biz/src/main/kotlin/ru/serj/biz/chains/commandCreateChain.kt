package ru.serj.biz.chains

import BirdsContext
import models.BirdsCommand.CREATE
import models.BirdsState.RUNNING
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.create(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) = chain {
    title = name
    description = "Подцепочка для обработки входящих запросов на создание"
    on {
        state == RUNNING && command == CREATE
    }
    block()
}