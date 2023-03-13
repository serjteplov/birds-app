
// проект содержит мапперы внутренних моделей (birds-api-common)
// в транспортные модели (birds-app-api)

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api"))
    implementation(project(":birds-app-common"))
    testImplementation(kotlin("test-junit"))
}