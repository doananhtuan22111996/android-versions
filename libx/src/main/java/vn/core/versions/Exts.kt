package vn.core.versions

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
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import vn.core.libx.BundleAlias
import vn.core.libx.DependencyModel

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.`implementation`(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.implementationPlatform(dependencyNotation: Any) {
    add("implementation", platform(dependencyNotation))
}

fun DependencyHandler.implementationGroupPlatform(dependencies: List<Any>) {
    for (d in dependencies) {
        add("implementation", platform(d))
    }
}

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.`debugImplementation`(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'releaseImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.`releaseImplementation`(dependencyNotation: Any): Dependency? =
    add("releaseImplementation", dependencyNotation)

/**
 * Adds a dependency to the `testImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.testImplementationGroup(dependencies: List<Any>) {
    for (d in dependencies) {
        add("testImplementation", d)
    }
}

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
 * Adds a dependency to the 'commonTestImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.`commonTestImplementation`(dependencyNotation: Any): Dependency? =
    add("commonTestImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'kapt' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.`kapt`(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

val Project.isAndroidApplication
    get() =
        pluginManager.hasPlugin("com.android.application")

val Project.isAndroidLibrary
    get() =
        pluginManager.hasPlugin("com.android.library")

val Project.isKotlinMultiplatformModule
    get() = kmpExtension != null

val Project.kmpExtension
    get() = extensions.findByType(KotlinMultiplatformExtension::class.java)

val KotlinMultiplatformExtension.hasAndroidTarget
    get() = targets.findByName("android") != null

val KotlinMultiplatformExtension.hasIosTarget
    get() = targets.findByName("iosArm64") != null ||
            targets.findByName("iosX64") != null

val KotlinMultiplatformExtension.hasJvmTarget
    get() = targets.findByName("jvm") != null

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
