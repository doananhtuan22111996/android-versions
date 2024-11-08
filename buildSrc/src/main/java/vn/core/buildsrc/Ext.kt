package vn.core.buildsrc

import org.gradle.api.Project
import java.io.File
import java.util.Properties

fun Project.getLocalProperty(propertyName: String): String {
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

fun Project.repository() = uri("https://maven.pkg.github.com/${username()}/android-versions")

fun Project.username() = System.getenv("GH_USERNAME") ?: getLocalProperty("GH_USERNAME")
fun Project.password() = System.getenv("GH_TOKEN") ?: getLocalProperty("GH_TOKEN")
