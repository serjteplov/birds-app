package ru.serj.dsl

import ru.serj.handlers.BaseWorker
import ru.serj.handlers.executeSequential

class ChainDsl<T> : BaseChainDsl<T>() {
    override var handler: suspend (T, List<BaseWorker<T>>) -> Unit = { a, b -> executeSequential(a, b) }
}
