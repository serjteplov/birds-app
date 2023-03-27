
plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "0.0.3"

dependencies {

    implementation("com.yandex.cloud:java-sdk-functions:2.5.1")

    val jacksonVersion: String by project
    val serializationVersion: String by project
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-mappers"))

    testImplementation(kotlin("test-junit"))
}

repositories {
    mavenLocal()
    mavenCentral()
}
