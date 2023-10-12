val kotlinVersion: String by project
val logbackVersion: String by project
val kotlinDatetime: String by project
val moreAppendersLogbackVersion: String by project
val fluentdLoggerVersion: String by project
val logstashEncoderVersion: String by project
val mockkVersion: String by project

plugins {
    `project-report`
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application {
    mainClass.set("ru.serj.birds.reactive.BirdsAppSpringWeb")
}

repositories {
    mavenLocal()
    mavenCentral()
    val repoUser: String? = System.getenv("GITHUB_ACTOR")
    val repoPass: String? = System.getenv("GITHUB_TOKEN")
    maven {
        url = uri("https://maven.pkg.github.com/serjteplov/birds-app")
        credentials {
            username = repoUser
            password = repoPass
        }
    }
}

dependencies {

    val coroutinesVersion: String by project
    val springdocOpenapiUiVersion: String by project
    val serializationVersion: String by project
    val assertjVersion: String by project
    val springmockkVersion: String by project

    implementation("org.springframework.boot:spring-boot-starter-actuator") // info; refresh; springMvc output
    implementation("org.springframework.boot:spring-boot-starter-webflux") // Controller, Service, etc..
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // from models to json and Vice versa
    implementation("org.jetbrains.kotlin:kotlin-reflect") // for spring-boot app
    implementation("org.jetbrains.kotlin:kotlin-stdlib") // for spring-boot app
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiUiVersion")


    implementation("ru.serj:birds-app-api-base-v1:${project.version}")
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-lib-logging-logback"))
    implementation(project(":birds-app-lib-logging-kermit"))
    implementation(project(":birds-app-lib-logging-common"))
    implementation(project(":birds-app-mappers"))
    implementation(project(":birds-app-biz"))
    implementation(project(":birds-app-domain-ehcache"))
    implementation(project(":birds-app-domain-postgres"))
    implementation(project(":birds-app-domain"))


    implementation(kotlin("stdlib"))
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.sndyuk:logback-more-appenders:$moreAppendersLogbackVersion")
    implementation("org.fluentd:fluent-logger:$fluentdLoggerVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:$mockkVersion")
}
