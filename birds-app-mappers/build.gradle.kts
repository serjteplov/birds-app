
// проект содержит мапперы внутренних моделей (birds-api-common)
// в транспортные модели (birds-app-api)

val kotlinDatetime: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    testImplementation(kotlin("test-junit"))
}