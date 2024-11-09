import vn.core.buildsrc.Configs
import vn.core.buildsrc.Configs.GITHUB_PACKAGES
import vn.core.buildsrc.Configs.VERSION_TOML
import vn.core.buildsrc.password
import vn.core.buildsrc.repository
import vn.core.buildsrc.username

plugins {
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

java {
    sourceCompatibility = Configs.javaVersion
    targetCompatibility = Configs.javaVersion
}

publishing {
    repositories {
        maven {
            name = GITHUB_PACKAGES
            url = repository()
            credentials {
                username = username()
                password = password()
            }
        }
    }
    publications {
        create<MavenPublication>("catalog") {
            from(components["versionCatalog"])
            groupId = Configs.Artifact.GROUP_ID // Replace with your GitHub username
            artifactId = Configs.Artifact.VERSION_ID
            version = Configs.Artifact.VERSION // Set your desired version here
        }
    }
}

versionCatalogs {
    catalog {
        versionCatalog {
            from(files(VERSION_TOML))
        }
    }
}
