package provider

interface BirdsLoggerCommon {

    fun log(
        msg: String?,
        level: String,
        throwable: Throwable? = null,
        data: Any? = null
    )

    fun info(msg: String?, data: Any? = null) {
        log(msg = msg, level = "INFO", data = data)
    }

    suspend fun doWithLogging(block: suspend () -> Any?) {
        info("Start")
        block()
        info("End")
    }
}