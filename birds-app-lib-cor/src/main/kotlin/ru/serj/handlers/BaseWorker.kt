package ru.serj.handlers

abstract class BaseWorker<T>(

    override val title: String,

    override val description: String,

    val on: T.() -> Boolean = { true },

    val except: T.(Throwable) -> Unit = {}

) : BaseHandler<T>
{
    abstract suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Exception) {
                context.except(e)
            }
        }
    }
}