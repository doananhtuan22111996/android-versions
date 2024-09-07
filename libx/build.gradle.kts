plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    val ghUsername = System.getenv("USERNAME")
    val ghPassword = System.getenv("TOKEN")
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
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact("../libx.versions.toml")
            groupId = "vn.core.libx" // Replace with your GitHub username
            artifactId = "versions"
            version = "1.0.0" // Set your desired version here
        }
    }
}