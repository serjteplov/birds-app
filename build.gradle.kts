import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val appCurrentVersion = project.properties["birds.app.currentVersion"] as String
val appJvmVersion = project.properties["birds.app.jvmTarget"] as String

plugins {
    kotlin("jvm") version "1.7.21"
}

allprojects {
    group = "ru.serj"
    version = appCurrentVersion
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = appJvmVersion
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
}

