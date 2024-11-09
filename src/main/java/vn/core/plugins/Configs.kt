package vn.core.plugins

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Configs {
    const val MIN_SDK = 26
    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MAVEN_DOMAIN = "https://maven.pkg.github.com"
    val jvmTarget = JvmTarget.JVM_11.target
    val javaVersion = JavaVersion.VERSION_11

    object Mode {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }
}