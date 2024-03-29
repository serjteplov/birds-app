
// Inspired by svok! https://github.com/crowdproj/crowdproj/tree/main/specs-v0

plugins {
    kotlin("jvm")
    `maven-publish`
}

// готовим артефакт, который состоит из одного файла bird-spec-base-v1.yml
val archives: Configuration by configurations.getting
val specFile = layout.buildDirectory.file("$projectDir/spec/bird-spec-base-v1.yml")
val specArtifact = artifacts.add(archives.name, specFile.get().asFile) {
    type = "yml"
    classifier = "openapi"
}

tasks {
    publish {
        dependsOn(build)
    }
}

publishing {
    // создаем публикацию и помещаем specArtifact в публикацию
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
            artifact(specArtifact)
            pom {
                scm {// инфо для справки
                    connection.set("scm:git:git://github.com/serjteplov/birds-app.git")
                    developerConnection.set("scm:git:ssh://github.com/serjteplov/birds-app.git")
                    url.set("https://github.com/serjteplov/birds-app")
                }
            }
        }
    }
    // публикуем на удаленный репозиторий (для публикации в localMaven эта секция не нужна)
    repositories {
        val repoHost = "https://maven.pkg.github.com/serjteplov/birds-app"
        val repoUser: String? = System.getenv("GITHUB_ACTOR")
        val repoPass: String? = System.getenv("GITHUB_TOKEN")
        println("REPO: $repoHost USER: $repoUser")
        if (repoUser != null && repoPass != null) {
            println("Birds-app: Credentials are OK!")
            maven {
                name = "GitHubPackages"
                url = uri(repoHost)
                credentials {
                    username = repoUser
                    password = repoPass
                }
            }
        }
    }
}
