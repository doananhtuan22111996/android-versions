package vn.core.versions

import dagger.hilt.android.plugin.HiltExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libx.mobilex

class AndroidHiltPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.apply(mobilex.plugins.androidHilt)

        if (isAndroidApplication) {
            hilt {
                enableAggregatingTask = true
            }
        }
        dependencies {
            catalog {
                implementation(bundle(mobilex.bundles.hiltComponents))
                implementation(mobilex.androidxHilt)
                kapt(mobilex.androidxHiltCompiler)

                testImplementation(mobilex.androidxHiltTesting)
                androidTestImplementation(mobilex.androidxHiltTesting)
            }
        }
    }
}

/**
 * Configures the [hilt][dagger.hilt.android.plugin.HiltExtension] extension.
 */
private fun Project.`hilt`(configure: Action<HiltExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("hilt", configure)
