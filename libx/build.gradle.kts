import vn.core.buildsrc.Configs
import vn.core.buildsrc.Configs.GITHUB_PACKAGES
import vn.core.buildsrc.Configs.GROUP_ID
import vn.core.buildsrc.Configs.VERSION_TOML
import vn.core.buildsrc.password
import vn.core.buildsrc.repository
import vn.core.buildsrc.username

plugins {
    alias(mobilex.plugins.jetbrainsKotlinJvm)
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

java {
    sourceCompatibility = Configs.javaVersion
    targetCompatibility = Configs.javaVersion
}

dependencies {
    implementation(project(":"))
    implementation(mobilex.kotlinStdlib)
    implementation(mobilex.androidBuildGradleApi)
    implementation(mobilex.kotlinGradlePlugin)
    implementation(mobilex.androidHiltGraldePlugin)
}

//gradlePlugin {
//    website = "https://github.com/doananhtuan22111996/android-versions"
//    vcsUrl = "https://github.com/doananhtuan22111996/android-versions.git"
//    plugins {
//        create("androidCorePlugin") {
//            group = "vn.core.pluginx"
//            id = "vn.core.pluginx.android-core"
//            displayName = "Plugin for compatibility testing of Gradle plugins"
//            description = "A plugin that helps you test your plugin against a variety of Gradle versions"
//            tags = listOf("testing", "integrationTesting", "compatibility")
//            implementationClass = "vn.core.versions.AndroidCorePlugin"
//            version = "1.0.0"
//        }
//    }
//}

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
            groupId = GROUP_ID // Replace with your GitHub username
            artifactId = Configs.Versions.ARTIFACT_ID
            version = Configs.Versions.VERSION // Set your desired version here
        }

//        create<MavenPublication>("androidPluginsPublication") {
//            from(components["java"])
//            groupId = GROUP_ID
//            artifactId = Configs.Plugins.ARTIFACT_ID
//            version = Configs.Plugins.VERSION
//        }
    }
}

versionCatalogs {
    catalog {
        versionCatalog {
            from(files(VERSION_TOML))
        }
    }
}
