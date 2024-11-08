import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import vn.core.buildsrc.Configs
import vn.core.buildsrc.SourceGeneratingTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(mobilex.plugins.jetbrainsKotlinJvm) apply false
    `kotlin-dsl`
}

java {
    sourceCompatibility = Configs.javaVersion
    targetCompatibility = Configs.javaVersion
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
    kotlinOptions.jvmTarget = Configs.javaVersion.toString()
    setDependsOn(listOf(tasks.named("generateDependenciesConfig")))
}
