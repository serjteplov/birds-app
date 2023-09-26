
// проект содержит мапперы внутренних моделей (birds-api-common)
// в транспортные модели (birds-app-api)

val kotlinDatetime: String by project
val mockkVersion: String by project

plugins {
    kotlin("jvm")
}

tasks.withType<Test> {
    jvmArgs = mutableListOf(
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang=ALL-UNNAMED"
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    implementation(project(":birds-app-domain"))

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation(kotlin("test-junit"))
}