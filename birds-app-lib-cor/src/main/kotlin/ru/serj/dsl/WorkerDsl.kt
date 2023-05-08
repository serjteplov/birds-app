package ru.serj.dsl

import ru.serj.handlers.BaseWorker
import ru.serj.handlers.Worker

class WorkerDsl<T> : BaseWorkerDsl<T>() {
    var blockOn: T.() -> Boolean = { true }
    var blockHandle: suspend T.() -> Unit = {}
    var blockExcept: T.(Throwable) -> Unit = {}

    override fun handle(block: suspend T.() -> Unit) {
        blockHandle = block
    }
    fun on(block: T.() -> Boolean) {
        blockOn = block
    }
    fun except(block: T.(Throwable) -> Unit) {
        blockExcept = block
    }
    override fun build(): BaseWorker<T> {
        return Worker(title, description, blockOn, blockHandle, blockExcept)
    }
}
