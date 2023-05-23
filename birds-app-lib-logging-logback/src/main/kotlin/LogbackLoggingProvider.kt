import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import provider.BirdsLoggerCommon
import provider.LoggingProvider
import kotlin.reflect.KClass

class LogbackLoggingProvider : LoggingProvider {

    override fun getLogger(id: String): BirdsLoggerCommon = BirdsLogbackLogger(LoggerFactory.getLogger(id) as Logger)

    override fun getLogger(clazz: KClass<*>): BirdsLoggerCommon = BirdsLogbackLogger(LoggerFactory.getLogger(clazz.java) as Logger)
}