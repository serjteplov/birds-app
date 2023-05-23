import provider.BirdsLoggerCommon
import provider.LoggingProvider
import kotlin.reflect.KClass

class KermitLoggingProvider : LoggingProvider {
    override fun getLogger(id: String): BirdsLoggerCommon {
        TODO("Not yet implemented")
    }

    override fun getLogger(clazz: KClass<*>): BirdsLoggerCommon {
        TODO("Not yet implemented")
    }

}