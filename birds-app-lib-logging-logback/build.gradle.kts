val logbackVersion: String by project
val kotlinDatetime: String by project
val logstashEncoderVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-lib-logging-common"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    testImplementation(kotlin("test-junit"))
}
