import java.util.Properties

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("maven-publish")
    id("version-catalog")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    val ghUsername = System.getenv("USERNAME") ?: getLocalProperty("USERNAME")
    val ghPassword = System.getenv("TOKEN") ?: getLocalProperty("TOKEN")
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${ghUsername}/android-versions")
            credentials {
                username = ghUsername
                password = ghPassword
            }
        }
    }
    publications {
        create<MavenPublication>("libsToml") {
            artifact(file("mobilex.versions.toml")) {
                extension = "toml"
            }
            groupId = "vn.core.libx" // Replace with your GitHub username
            artifactId = "versions"
            version = "1.0.0" // Set your desired version here
        }
    }
}

fun getLocalProperty(propertyName: String): String {
    val localProperties = Properties().apply {
        val localPropertiesFile = File(rootDir, "local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        }
    }

    return localProperties.getProperty(propertyName) ?: run {
        throw NoSuchFieldException("Not defined property: $propertyName")
    }
}