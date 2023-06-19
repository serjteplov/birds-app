import ch.qos.logback.classic.Logger
import kotlinx.datetime.Clock
import org.slf4j.Marker
import org.slf4j.event.KeyValuePair
import org.slf4j.event.LoggingEvent
import org.slf4j.helpers.BasicMarkerFactory
import provider.BirdsLoggerCommon
import net.logstash.logback.argument.StructuredArguments

class BirdsLogbackLogger(
    private val logger: Logger
) : BirdsLoggerCommon {

    override fun log(
        msg: String?,
        level: String,
        throwable: Throwable?,
        data: Any?
    ) {
        logger.log(
            object: LoggingEvent {
                override fun getLevel() = level.toLevel()
                override fun getLoggerName(): String = logger.name
                override fun getMessage() = msg
                override fun getArguments(): MutableList<Any?> = argumentArray.toMutableList()
                override fun getThreadName() = "THREAD-NAME-BIRDS"
                override fun getArgumentArray(): Array<Any?> = arrayOf(StructuredArguments.keyValue("data",data))
                override fun getMarkers(): MutableList<Marker> = mutableListOf(BasicMarkerFactory().getMarker("BIRDS-MARKER"))
                override fun getKeyValuePairs(): MutableList<KeyValuePair> = mutableListOf(KeyValuePair("fffff", "bbbbbb"))
                override fun getTimeStamp() = Clock.System.now().epochSeconds
                override fun getThrowable() = throwable
            }
        )
    }

    fun String.toLevel() =
        when (this) {
            "INFO" -> org.slf4j.event.Level.INFO
            "DEBUG" -> org.slf4j.event.Level.DEBUG
            "ERROR" -> org.slf4j.event.Level.ERROR
            "TRACE" -> org.slf4j.event.Level.TRACE
            "WARN" -> org.slf4j.event.Level.WARN
            else -> throw RuntimeException("unknown level: $this")
        }
}