import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import vn.core.buildsrc.SourceGeneratingTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(mobilex.plugins.jetbrainsKotlinJvm) apply false
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    sourceSets {
        main.configure {
            kotlin.srcDir("build/configs")
        }
    }
}

tasks.register<SourceGeneratingTask>("sourceGeneratingTask") {
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
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    setDependsOn(listOf(tasks.named("generateDependenciesConfig")))
}
