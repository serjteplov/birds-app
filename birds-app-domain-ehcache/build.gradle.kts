val kotlinVersion: String by project
val kotlinDatetime: String by project
val kotlinLogging: String by project
val ehcacheVersion: String by project
val junitJupiterVersion: String by project
val mockkVersion: String by project

plugins {
    kotlin("jvm")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-domain"))

    implementation("org.ehcache:ehcache:$ehcacheVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
}
