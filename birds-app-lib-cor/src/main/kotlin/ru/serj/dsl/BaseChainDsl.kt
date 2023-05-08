package ru.serj.dsl

import ru.serj.handlers.BaseWorker
import ru.serj.handlers.Chain

abstract class BaseChainDsl<T> : BaseWorkerDsl<T>() {
    var blockOn: T.() -> Boolean = { true }
    var blockEx: T.(Throwable) -> Unit = {}
    var workers: MutableList<BaseWorkerDsl<T>> = mutableListOf()
    abstract var handler: suspend (T, List<BaseWorker<T>>) -> Unit

    override fun handle(block: suspend T.() -> Unit) {}

    fun on(block: T.() -> Boolean) {
        blockOn = block
    }
    fun worker(block: WorkerDsl<T>.() -> Unit) {
        val workerDsl1 = WorkerDsl<T>()
        workerDsl1.block()
        workers.add(workerDsl1)
    }
    fun worker(title: String, block: suspend T.() -> Unit) {
        val workerDsl1 = WorkerDsl<T>()
        workerDsl1.blockHandle = block
        workerDsl1.title = title
        workers.add(workerDsl1)
    }
    fun chain(block: BaseChainDsl<T>.() -> Unit) {
        val chainDsl = ChainDsl<T>()
        chainDsl.block()
        workers.add(chainDsl)
    }

    fun parallel(block: ParallelDsl<T>.() -> Unit) {
        val parallelDsl = ParallelDsl<T>()
        parallelDsl.block()
        workers.add(parallelDsl)
    }
    override fun build(): Chain<T> {
        val execs = workers.map { it.build() }
        return Chain(title, description, blockOn, blockEx, handler, execs)
    }
    fun printResult() {
        workers.forEach { println(it.description) }
    }
}
