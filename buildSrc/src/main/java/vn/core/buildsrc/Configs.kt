package vn.core.buildsrc

import org.gradle.api.JavaVersion

object Configs {
    const val GITHUB_PACKAGES = "GitHubPackages"
    const val GROUP_ID = "vn.core.libx"
    const val VERSION_TOML = "mobilex.versions.toml"
    val javaVersion = JavaVersion.VERSION_11

    object Versions {
        const val ARTIFACT_ID = "versions"
        const val VERSION = "1.0.4"
    }

    object Plugins {
        const val ARTIFACT_ID = "plugins"
        const val VERSION = "1.0.0"
    }
}

