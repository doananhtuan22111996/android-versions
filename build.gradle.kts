import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import vn.core.buildsrc.SourceGeneratingTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(mobilex.plugins.jetbrainsKotlinJvm) apply false
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
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
//
//publishing {
//    repositories {
//        maven {
//            name = GITHUB_PACKAGES
//            url = repository()
//            credentials {
//                username = username()
//                password = password()
//            }
//        }
//    }
//    publications {
//        create<MavenPublication>("gpr") {
//            from(components["java"])
//            group = GROUP_ID
//            artifactId = "pluginx"
//            version = "1.0.0" // Set your desired version here
//        }
//    }
//}

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
