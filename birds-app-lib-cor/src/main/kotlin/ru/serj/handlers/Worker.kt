package ru.serj.handlers

class Worker<T>(

    val title1: String,

    val description1: String,

    val on1: T.() -> Boolean = { true },

    val handle1: suspend T.() -> Unit = {},

    val except1: T.(Throwable) -> Unit = {}

) : BaseWorker<T>(title1, description1, on1, except1) {

    override suspend fun handle(context: T) = handle1(context)
}