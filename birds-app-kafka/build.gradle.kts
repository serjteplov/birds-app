val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val jacksonVersion: String by project
val kotlinDatetime: String by project
val kotlinLogging: String by project
val coroutinesVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.apache.kafka:kafka-clients:3.4.0")

    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("io.github.microutils:kotlin-logging:$kotlinLogging")



    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")


    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-mappers"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation(kotlin("test-junit"))
}
