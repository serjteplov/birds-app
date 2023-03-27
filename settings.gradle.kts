rootProject.name = "birds-app"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("m1l1-quickstart")
include("m1l3-oop")
include("m1l4-dsl")
include("m1l5-coroutines")
include("m1l6-flow")
include("birds-app-common")
include("birds-app-api-v1")
include("birds-app-api-base-v1")
include("birds-app-mappers")
include("birds-app-ktor")
include("birds-app-serverless")
