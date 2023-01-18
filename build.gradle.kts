import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val appCurrentVersion = project.properties["birds.app.currentVersion"] as String
val appJvmVersion = project.properties["birds.app.jvmTarget"] as String

plugins {
    kotlin("jvm") version Version.KOTLIN apply false
}

allprojects {
    group = "ru.serj"
    version = Version.BIRDS_APP
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = Version.JVM_TARGET
    }
    repositories {
        mavenCentral()
    }
}

repositories {
    appJvmVersion
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
}

