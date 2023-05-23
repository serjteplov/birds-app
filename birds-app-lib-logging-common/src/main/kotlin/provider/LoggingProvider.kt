package provider

import kotlin.reflect.KClass

interface LoggingProvider {

    fun getLogger(id: String): BirdsLoggerCommon

    fun getLogger(clazz: KClass<*>): BirdsLoggerCommon
}