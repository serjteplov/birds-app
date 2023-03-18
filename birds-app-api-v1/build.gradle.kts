
// проект содержит транспортные модели в виде openapi спеки,
// а также сгенерированные по данной спеке классы

plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

// помечаем сгенерированные модели как source
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
    this.openApiGenerate {
        dependsOn(getSpecTask)
    }
}

val apiSpec: Configuration by configurations.creating

// копируем базовую спеку к себе в класспас, далее будем обращаться к ней в спеке
val getSpecTask by tasks.creating {
    doFirst {
        copy {
            from(apiSpec.asPath)
            into("$buildDir")
            rename { "base.yml" }
        }
    }
}

dependencies {
    val jacksonVersion: String by project
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testImplementation(kotlin("test-junit"))
    // импортируем проект из localmaven с базовой спекой birds-app-api-base
    apiSpec(
        group = rootProject.group.toString(),
        name = "birds-app-api-base",
        version = rootProject.version.toString(),
        classifier = "openapi",
        ext = "yml"
    )
}

repositories {
    mavenLocal()
    mavenCentral()
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