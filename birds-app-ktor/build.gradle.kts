
plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(kotlin("stdlib"))
    implementation(project(":birds-app-api-v1"))
    implementation(project(":birds-app-common"))
    testImplementation(kotlin("test-junit"))
}


// hello world на кторе
// добавить просто ктор без кодеина
// довести до рабочего состояния
// добавить кодеин