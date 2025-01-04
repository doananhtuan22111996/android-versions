package vn.core.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import vn.core.libs.BundleAlias
import vn.core.libs.DependencyModel
import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * Configures the [android][com.android.build.gradle.LibraryExtension] extension.
 */
internal fun Project.android(configure: Action<BaseAppModuleExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("android", configure)

internal fun BaseAppModuleExtension.kotlinOptions(configure: Action<KotlinJvmOptions>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlinOptions", configure)

/**
 * Configures the [kapt][org.jetbrains.kotlin.gradle.plugin.KaptExtension] extension.
 */
internal fun Project.kapt(configure: Action<KaptExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kapt", configure)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

/**
 * Adds a dependency to the `testImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

/**
 * Adds a dependency to the `androidTestImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'kapt' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

fun Project.catalog(block: Sequence<VersionCatalog>.() -> Unit = {}): Sequence<VersionCatalog> =
    try {
        mutableListOf<VersionCatalog>().apply {
            val catalogsExtension = extensions
                .getByType<VersionCatalogsExtension>()
            catalogsExtension.catalogNames
                .map { catalogsExtension.named(it) }
                .sortedBy { it.name }
                .reversed()
                .also { addAll(it) }
        }.asSequence().apply(block)
    } catch (e: Throwable) {
        emptySequence()
    }

fun Sequence<VersionCatalog>.get(model: DependencyModel): Provider<MinimalExternalModuleDependency> =
    map { it.findLibrary(model.libraryAlias) }
        .first { it.orElse(null) != null }
        .get()

fun Sequence<VersionCatalog>.version(versionAlias: String): String =
    map { it.findVersion(versionAlias) }
        .first { it.orElse(null) != null }
        .get().requiredVersion

fun Sequence<VersionCatalog>.bundle(bundleAlias: BundleAlias): Provider<ExternalModuleDependencyBundle> =
    map { it.findBundle(bundleAlias) }
        .first { it.orElse(null) != null }
        .get()

fun getSecretPropertyFile(rootProject: Project): Properties {
    val signingKeyAlias = "SIGNING_KEY_ALIAS"
    val signingKeystorePassword = "SIGNING_KEYSTORE_PASSWORD"
    val signingKeyPassword = "SIGNING_KEY_PASSWORD"

    val secretPropertiesFile: File = rootProject.file("secret.properties")
    val secretProperties = Properties()
    if (secretPropertiesFile.exists()) {
        secretProperties.load(FileInputStream(secretPropertiesFile))
    }
    System.getenv(signingKeyAlias)?.let {
        secretProperties.setProperty(signingKeyAlias, it)
    }
    System.getenv(signingKeystorePassword)?.let {
        secretProperties.setProperty(signingKeystorePassword, it)
    }
    System.getenv(signingKeyPassword)?.let {
        secretProperties.setProperty(signingKeyPassword, it)
    }
    return secretProperties
}
