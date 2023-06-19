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
    group = rootProject.group
    version = rootProject.version
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

