import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import vn.core.buildsrc.Configs
import vn.core.buildsrc.Configs.GITHUB_PACKAGES
import vn.core.buildsrc.SourceGeneratingTask
import vn.core.buildsrc.password
import vn.core.buildsrc.repository
import vn.core.buildsrc.username

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = Configs.Artifact.GROUP_ID
version = Configs.Artifact.VERSION // Set your desired version here

java {
    sourceCompatibility = Configs.javaVersion
    targetCompatibility = Configs.javaVersion
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    sourceSets {
        main.configure {
            kotlin.srcDir("build/configs")
        }
    }
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "vn.core.plugins.androidApplication"
            displayName = "Android Application"
            description = "Plugin for android application module"
            implementationClass = "vn.core.plugins.AndroidApplicationPlugin"
        }
        create("androidLibrary") {
            id = "vn.core.plugins.androidLibrary"
            displayName = "Android Library"
            description = "Plugin for android library module"
            implementationClass = "vn.core.plugins.AndroidLibraryPlugin"
        }
        create("androidPublishing") {
            id = "vn.core.plugins.androidPublishing"
            displayName = "Android Publishing"
            description = "Plugin for android publishing module"
            implementationClass = "vn.core.plugins.AndroidPublishingPlugin"
        }
    }
}

dependencies {
    api(mobilex.androidBuildGrade)
    api(mobilex.androidBuildGradleApi)
    api(mobilex.kotlinStdlib)
    api(mobilex.kotlinGradlePlugin)
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
        create<MavenPublication>("plugins") {
            from(components["java"])
            artifactId = Configs.Artifact.PLUGIN_ID
        }
    }
}

tasks.register<SourceGeneratingTask>("sourceGeneratingTask") {
    setDependsOn(
        listOf(
            tasks.named("sourcesJar")
        )
    )
    this.outputFile.set(
        File(project.rootDir.absolutePath, "build/configs")
    )
}

tasks.create("generateDependenciesConfig") {
    setDependsOn(
        listOf(
            tasks.named("clean"), tasks.named("sourceGeneratingTask")
        )
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = Configs.javaVersion.toString()
    setDependsOn(listOf(tasks.named("generateDependenciesConfig")))
}
