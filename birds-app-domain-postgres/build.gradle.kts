val kotlinVersion: String by project
val kotlinDatetime: String by project
val postgresqlVersion: String by project
val kotlinExposedVersion: String by project
val testcontainersPostgresqlVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-mappers"))
    implementation(project(":birds-app-domain"))

    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.jetbrains.exposed:exposed-core:$kotlinExposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$kotlinExposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$kotlinExposedVersion")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$kotlinExposedVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")

    implementation("org.testcontainers:postgresql:$testcontainersPostgresqlVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}
