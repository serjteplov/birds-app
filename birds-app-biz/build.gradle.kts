val coroutinesVersion: String by project
val kotlinLogging: String by project
val logbackVersion: String by project
val kotlinDatetime: String by project
val junitJupiterVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-mappers"))
    implementation(project(":birds-app-lib-cor"))
    implementation(project(":birds-app-domain-ehcache"))
    implementation(project(":birds-app-domain"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLogging")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
    implementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

    testImplementation(kotlin("test-junit"))
}
