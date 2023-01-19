import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val appCurrentVersion = project.properties["birds.app.currentVersion"] as String
val appJvmVersion = project.properties["birds.app.jvmTarget"] as String

plugins {
    kotlin("jvm")
}

group = "ru.serj"
version = Version.BIRDS_APP

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = Version.JVM_TARGET
    }
}

