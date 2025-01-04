package vn.core.buildsrc

import org.gradle.api.JavaVersion

object Configs {
    const val GITHUB_PACKAGES = "Packages"
    const val VERSION_TOML = "mobilex.versions.toml"
    val javaVersion = JavaVersion.VERSION_11

    object Artifact {
        const val GROUP_ID = "vn.core.libs"
        const val VERSION_ID = "versions"
        const val PLUGIN_ID = "plugins"
        const val VERSION = "1.0.2"
    }
}
