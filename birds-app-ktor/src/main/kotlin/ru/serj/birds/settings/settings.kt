package ru.serj.birds.settings

import KermitLoggingProvider
import LogbackLoggingProvider
import io.ktor.server.application.*
import provider.LoggingProvider
import settings.BirdsLoggerSettings
import ru.serj.biz.processor.BirdsMainProcessor
import ru.serj.domain.entity.SqlProperties
import ru.serj.domain.repository.BirdsTweetRepository
import ru.serj.`in`.memory.repository.InMemoryTweetRepository
import ru.serj.postgres.repository.BirdsPostgresRepository

fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        corSettings = BirdsMainProcessor(
            repository = chooseDBMS()
        ),
        logSettings = BirdsLoggerSettings(
            loggerProvider = chooseLoggingProvider()
        )
    )
}

fun Application.getAuth(): AuthSettings {
    val issuer = environment.config.propertyOrNull("jwt.issuer")?.getString() ?: ""
    val audience = environment.config.propertyOrNull("jwt.audience")?.getString() ?: ""
    val realm = environment.config.propertyOrNull("jwt.realm")?.getString() ?: ""
    val secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: ""
    val certPath = environment.config.propertyOrNull("jwt.certPath")?.getString() ?: ""
    return AuthSettings(issuer, audience, realm, secret, certPath)
}

fun Application.chooseLoggingProvider(): LoggingProvider {
    return when (environment.config.propertyOrNull("logging.provider")?.getString()) {
        "logback" -> LogbackLoggingProvider()
        "kermit" -> KermitLoggingProvider()
        else -> throw RuntimeException("bad logging provider")
    }
}

fun Application.chooseDBMS(): BirdsTweetRepository {
    val url = environment.config.propertyOrNull("dbms.url")?.getString()
    val dbHost = environment.config.propertyOrNull("dbms.dbhost")?.getString()
    val dbPort = environment.config.propertyOrNull("dbms.dbport")?.getString()
    val user = environment.config.propertyOrNull("dbms.user")?.getString()
    val driver = environment.config.propertyOrNull("dbms.driver")?.getString()
    val password = environment.config.propertyOrNull("dbms.password")?.getString()
    val database = environment.config.propertyOrNull("dbms.database")?.getString()
    return when (environment.config.propertyOrNull("dbms.vendor")?.getString()) {
        "in-memory" -> InMemoryTweetRepository()
        "postgres" -> BirdsPostgresRepository(
            SqlProperties(
                url = "$url$dbHost:$dbPort/$database",
                user = requireNotNull(user),
                password = requireNotNull(password),
                driver = requireNotNull(driver),
            )
        )
        "mongo" -> InMemoryTweetRepository()
        else -> throw RuntimeException("no valid dbms vendor specified")
    }
}
