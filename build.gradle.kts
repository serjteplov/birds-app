import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

allprojects {
    group = "ru.serj"
    version = "1.0-SNAPSHOT"
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

}

subprojects {
    repositories {
        mavenCentral()
    }
}

