import vn.core.buildsrc.Artifact.ARTIFACT_ID
import vn.core.buildsrc.Artifact.VERSION
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":"))
    implementation(mobilex.kotlinStdlib)
    implementation(mobilex.androidBuildGradleApi)
    implementation(mobilex.kotlinGradlePlugin)
    implementation(mobilex.androidHiltGraldePlugin)
}

// TODO
//gradlePlugin {
//    plugins {
//        create("androidCorePlugin") {
//            id = "vn.core.pluginx.android-core"
//            implementationClass = "vn.core.versions.AndroidCorePlugin"
//        }
//        create("androidComposePlugin") {
//            id = "vn.core.pluginx.android-compose"
//            implementationClass = "vn.core.versions.AndroidComposePlugin"
//        }
//        create("androidHiltPlugin") {
//            id = "vn.core.pluginx.hilt"
//            implementationClass = "vn.core.versions.AndroidHiltPlugin"
//        }
//        create("androidNavigationPlugin") {
//            id = "vn.core.pluginx.navigation"
//            implementationClass = "vn.core.versions.AndroidNavigationPlugin"
//        }
//        create("firebasePlugin") {
//            id = "vn.core.pluginx.firebase"
//            implementationClass = "vn.core.versions.AndroidFirebasePlugin"
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
            artifactId = ARTIFACT_ID
            version = VERSION // Set your desired version here
        }

        // TODO
        // Define a publication for each plugin
//        create<MavenPublication>("androidPluginsPublication") {
//            from(components["java"])
//            groupId = GROUP_ID
//            artifactId = "plugins"
//            version = "1.0.0"
//            pom {
//                name.set("Android Plugins Group")
//                description.set("A group of Android-related plugins including core, compose, hilt, navigation, and firebase.")
//            }
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

tasks.withType<PublishToMavenRepository>().configureEach {
    if (name.contains("PluginMarkerMavenPublicationToGitHubPackagesRepository")) {
        enabled = false
    }
}