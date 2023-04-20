package ru.serj.handlers

class Chain<T>(

    override val title: String,

    override val description: String,

    val chainOn: T.() -> Boolean = { true },

    val chainExcept: T.(Throwable) -> Unit = {},

    val handler: suspend (T, List<BaseWorker<T>>) -> Unit,

    val execs: List<BaseWorker<T>>,

) : BaseWorker<T>(title, description, chainOn, chainExcept) {

    override suspend fun handle(context: T) = handler(context, execs)
}
