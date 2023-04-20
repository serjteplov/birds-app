package ru.serj.dsl

import ru.serj.handlers.*

abstract class BaseWorkerDsl<T> {
    open var title: String = "Base dsl title"
    open var description: String = "Base dsl description"

    abstract suspend fun handle(block: suspend T.() -> Unit)
}

class WorkerDsl<T> : BaseWorkerDsl<T>() {
    var blockOn: T.() -> Boolean = { true }
    var blockHandle: suspend T.() -> Unit = {}
    var blockExcept: T.(Throwable) -> Unit = {}

    override suspend fun handle(block: suspend T.() -> Unit) {
        blockHandle = block
    }

    fun on(block: T.() -> Boolean) {
        blockOn = block
    }

    fun except(block: T.(Throwable) -> Unit) {
        blockExcept = block
    }
}

class ChainDsl<T> : BaseWorkerDsl<T>() {
    var blockOn: T.() -> Boolean = { true }
    var blockEx: T.(Throwable) -> Unit = {}
    var workers: MutableList<WorkerDsl<T>> = mutableListOf()
    private var handler: suspend (T, List<BaseWorker<T>>) -> Unit = { a, b -> executeSequential(a, b) }

    override suspend fun handle(block: suspend T.() -> Unit) {}

    fun on(block: T.() -> Boolean) {
        blockOn = block
    }

    fun worker(
        title: String,
        description: String = "Дефолтное описание последовательного воркера",
        block: suspend T.() -> Unit
    ) {
        val workerDsl = WorkerDsl<T>()
        workerDsl.blockHandle = block
        workerDsl.title = title
        workerDsl.description = description
        workers.add(workerDsl)
    }

    fun build(): Chain<T> {
        val execs = workers.map { it.build() }
        return Chain(title, description, blockOn, blockEx, handler, execs)
    }
}

class ParallelDsl<T> : BaseWorkerDsl<T>() {
    var blockOn: (T) -> Boolean = { true }
    var blockEx: T.(Throwable) -> Unit = {}
    var workers: MutableList<WorkerDsl<T>> = mutableListOf()
    private var handler: suspend (T, List<BaseWorker<T>>) -> Unit = { a, b -> executeParallel(a, b) }

    override suspend fun handle(block: suspend T.() -> Unit) {}

    fun on(block: T.() -> Boolean) {
        blockOn = block
    }

    fun worker(
        title: String,
        description: String = "Дефолтное описание параллельного воркера",
        block: suspend T.() -> Unit
    ) {
        val workerDsl = WorkerDsl<T>()
        workerDsl.blockHandle = block
        workerDsl.title = title
        workerDsl.description = description
        workers.add(workerDsl)
    }

    fun build(): Chain<T> {
        val execs = workers.map { it.build() }
        return Chain(title, description, blockOn, blockEx, handler, execs)
    }
}

class RootChainDsl<T> {
    var execs: MutableList<BaseWorker<T>> = mutableListOf()

    suspend fun worker(block: suspend WorkerDsl<T>.() -> Unit) {
        val workerDsl1 = WorkerDsl<T>()
        workerDsl1.block()
        execs.add(workerDsl1.build())
    }

    suspend fun chain(block: suspend ChainDsl<T>.() -> Unit) {
        val chainDsl = ChainDsl<T>()
        chainDsl.block()
        execs.add(chainDsl.build())
    }

    suspend fun parallel(block: suspend ParallelDsl<T>.() -> Unit) {
        val parallelDsl = ParallelDsl<T>()
        parallelDsl.block()
        execs.add(parallelDsl.build())
    }

    fun printResult() {
        execs.forEach { println(it.description) }
    }
}

suspend fun <T> rootChain(block: suspend RootChainDsl<T>.() -> Unit): RootChainDsl<T> {
    val rootChainDsl = RootChainDsl<T>()
    block(rootChainDsl)
    return rootChainDsl
}

suspend fun <T> RootChainDsl<T>.build(): Chain<T> =
    Chain(
        title = "Корневая цепочка",
        description = "Корневая цепочка (описание)",
        chainOn = { true },
        chainExcept = {},
        handler = { a, b -> executeSequential(a, b) },
        execs = execs
    )

fun <T> WorkerDsl<T>.build(): Worker<T> =
    Worker(
        title1 = title,
        description1 = description,
        on1 = blockOn,
        handle1 = blockHandle,
        except1 = blockExcept
    )
