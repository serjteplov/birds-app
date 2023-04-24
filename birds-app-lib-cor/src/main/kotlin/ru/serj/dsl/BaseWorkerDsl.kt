package ru.serj.dsl

import ru.serj.handlers.BaseWorker

abstract class BaseWorkerDsl<T> {
    open var title: String = "BaseWorkerDsl title"
    open var description: String = "BaseWorkerDsl description"
    abstract fun handle(block: suspend T.() -> Unit)
    abstract fun build(): BaseWorker<T>
}
