val kotlinVersion: String by project
val logbackVersion: String by project
val jacksonVersion: String by project
val kotlinDatetime: String by project

plugins {
    kotlin("jvm")
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-lib-logging-common"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation(kotlin("test-junit"))
}
