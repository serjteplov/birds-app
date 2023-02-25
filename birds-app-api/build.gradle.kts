plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val jacksonVersion: String by project
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testImplementation(kotlin("test-junit"))
}

// делаем сгенерированные модели видимыми при разработке
sourceSets {
    main {
        java.srcDir("$buildDir/generate-resources/main/src/main/kotlin/ru/serj/api/v1/models")
    }
}

// деалаем чтобы модели по openapi генерировались ДО компиляции
tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}

// https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$projectDir/spec/bird-spec-v1.yml")

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}
