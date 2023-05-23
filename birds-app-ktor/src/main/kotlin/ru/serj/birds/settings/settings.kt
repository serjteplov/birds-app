package ru.serj.birds.settings

import KermitLoggingProvider
import LogbackLoggingProvider
import io.ktor.server.application.*
import provider.LoggingProvider
import settings.BirdsLoggerSettings
import ru.serj.biz.processor.BirdsMainProcessor


fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        corSettings = BirdsMainProcessor(),
        logSettings = BirdsLoggerSettings(
            loggerProvider = chooseLoggingProvider()
        )
    )
}

fun Application.chooseLoggingProvider(): LoggingProvider {
    return when (environment.config.propertyOrNull("logging.provider")?.getString()) {
        "logback" -> LogbackLoggingProvider()
        "kermit" -> KermitLoggingProvider()
        else -> throw RuntimeException("bad logging provider")
    }
}
