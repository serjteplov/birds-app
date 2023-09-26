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
        // в данном репозитории лежит birds-app-api-base-v1
        maven {
            url = uri("https://maven.pkg.github.com/serjteplov/birds-app")
            val githubUsername = System.getenv("GITHUB_ACTOR")
            val githubToken = System.getenv("GITHUB_TOKEN")
            githubUsername?.let {
                credentials {
                    username = githubUsername
                    password = githubToken
                }
            }
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}

