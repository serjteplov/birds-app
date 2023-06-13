val kotlinDatetime: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDatetime")
}
