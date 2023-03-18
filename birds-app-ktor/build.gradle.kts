val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
}

dependencies {

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml-jvm:$ktor_version")


    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    testImplementation(kotlin("test-junit"))
}


// hello world на кторе
// добавить просто ктор без кодеина
// довести до рабочего состояния
// добавить кодеин