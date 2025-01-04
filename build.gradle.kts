import com.diffplug.gradle.spotless.SpotlessExtension
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
    alias(mobilex.plugins.spotless) apply false
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
        create("androidCompose") {
            id = "vn.core.plugins.androidCompose"
            displayName = "Android Compose"
            description = "Plugin for android compose module"
            implementationClass = "vn.core.plugins.AndroidComposePlugin"
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
    implementation(mobilex.androidBuildGrade)
    implementation(mobilex.androidBuildGradleApi)
    implementation(mobilex.kotlinStdlib)
    implementation(mobilex.kotlinGradlePlugin)
    implementation(mobilex.javaPoet)
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
            tasks.named("sourcesJar"),
        ),
    )
    this.outputFile.set(
        File(project.rootDir.absolutePath, "build/configs"),
    )
}

tasks.create("generateDependenciesConfig") {
    setDependsOn(
        listOf(
            tasks.named("clean"),
            tasks.named("sourceGeneratingTask"),
        ),
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = Configs.javaVersion.toString()
    setDependsOn(listOf(tasks.named("generateDependenciesConfig")))
}

allprojects {
    apply {
        plugin(rootProject.mobilex.plugins.spotless.get().pluginId)
    }

    configure<SpotlessExtension> {
        // Configuration for Java files
        java {
            target("**/*.java")
            googleJavaFormat().aosp() // Use Android Open Source Project style
            removeUnusedImports() // Automatically remove unused imports
            trimTrailingWhitespace() // Remove trailing whitespace
        }

        // Configuration for Kotlin files
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt") // Exclude files in the build directory
            ktlint("1.2.1").setEditorConfigPath(rootProject.file(".editorconfig").path) // Use ktlint with version 1.2.1 and custom .editorconfig
            toggleOffOn() // Allow toggling Spotless off and on within code files using comments
            trimTrailingWhitespace()
        }

        // Additional configuration for Kotlin Gradle scripts
        kotlinGradle {
            target("*.gradle.kts")
            ktlint("1.2.1") // Apply ktlint to Gradle Kotlin scripts
        }
    }
}
