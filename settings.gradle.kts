rootProject.name = "birds-app"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

include("m1l1-quickstart")
include("m1l3-oop")
