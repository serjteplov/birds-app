
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
    // помещаем созданный артефакт в публикацию
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
            artifact(specArtifact)
            pom {
                scm {
                    connection.set("scm:git:git://github.com/ccc/ccc.git")
                    developerConnection.set("scm:git:ssh://github.com/ccc/ccc.git")
                    url.set("https://github.com/ccc/ccc")
                }
            }
        }
    }
    // публикуем на удаленный репозиторий, для публикации в localMaven эта секция не нужна
    repositories {
        val repoHost: String = "https://maven.pkg.github.com/serjteplov/birds-app"
        val repoUser: String? = System.getenv("GITHUB_ACTOR")
        val repoPass: String? = System.getenv("GITHUB_TOKEN")
//        val repoUser: String? = "serjteplov"
//        val repoPass: String? = "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"

        println("1")
        repoUser?.also { println(it) }
        repoPass?.also { println(it) }
        println("2")

        if (repoUser != null && repoPass != null) {
            println("3")
            println("REPO: $repoHost USER: $repoUser")
            maven {
                name = "GitHubPackages"
                url = uri(repoHost)
                credentials {
                    username = repoUser
                    password = repoPass
                }
            }
        }
        println("4")
    }
}
