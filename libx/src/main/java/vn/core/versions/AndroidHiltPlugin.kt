package vn.core.versions

import dagger.hilt.android.plugin.HiltExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libx.libs

class AndroidHiltPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.apply(libs.plugins.androidHilt)

        if (isAndroidApplication) {
            hilt {
                enableAggregatingTask = true
            }
        }
        dependencies {
            catalog {
                implementation(bundle(libs.bundles.hiltComponents))
                implementation(libs.androidxHilt)
                kapt(libs.androidxHiltCompiler)

                testImplementation(libs.androidxHiltTesting)
                androidTestImplementation(libs.androidxHiltTesting)
            }
        }
    }
}

/**
 * Configures the [hilt][dagger.hilt.android.plugin.HiltExtension] extension.
 */
private fun Project.`hilt`(configure: Action<HiltExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("hilt", configure)
