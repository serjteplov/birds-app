
// данный проект содержит внутренние модели

val kotlinDatetime: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
    testImplementation(kotlin("test-junit"))
}
