val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val kotlinDatetime: String by project
val javaVersion = project.properties["birds.app.jvmTarget"] as String

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.ktor.plugin")
}

application {
    mainClass.set("ru.serj.birds.ApplicationKt")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(io.ktor.plugin.features.JreVersion.valueOf("JRE_$javaVersion"))
    }
}

repositories {
    mavenLocal()
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

dependencies {

    implementation(kotlin("stdlib"))

    implementation("ru.serj:birds-app-api-base-v1:${project.version}")
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-lib-logging-logback"))
    implementation(project(":birds-app-lib-logging-kermit"))
    implementation(project(":birds-app-lib-logging-common"))
    implementation(project(":birds-app-mappers"))
    implementation(project(":birds-app-biz"))

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-config-yaml-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-locations-jvm:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.sndyuk:logback-more-appenders:1.8.8")
    implementation("org.fluentd:fluent-logger:0.3.4")
    implementation("net.logstash.logback:logstash-logback-encoder:7.3")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation(kotlin("test-junit"))
}
