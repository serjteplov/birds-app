rootProject.name = "birds-app"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val ktorVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val springBootVersion: String by settings
    val springPluginVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false

        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version springPluginVersion apply false
    }
}

include("birds-app-common")
include("birds-app-api-v1")
include("birds-app-api-base-v1")
include("birds-app-mappers")
include("birds-app-ktor")
include("birds-app-kafka")
include("birds-app-serverless")
include("birds-app-lib-cor")
include("birds-app-biz")
include("birds-app-lib-logging-common")
include("birds-app-lib-logging-logback")
include("birds-app-lib-logging-kermit")
include("birds-app-domain")
include("birds-app-domain-ehcache")
include("birds-app-domain-postgres")
include("birds-app-spring-webflux")
