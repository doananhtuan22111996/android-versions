package vn.core.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import vn.core.libs.mobilex

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.run {
            apply(mobilex.plugins.composeCompiler)
        }

        androidConfig()

        dependencies {
            catalog {
                implementation(platform(get(mobilex.androidxComposeBom)))
                implementation(bundle(mobilex.bundles.jetpackComposeComponents))
                androidTestImplementation(bundle(mobilex.bundles.composeTestComponents))
            }
        }
    }

    private fun Project.androidConfig() {
        android {
            compileSdk = Configs.COMPILE_SDK

            defaultConfig {
                minSdk = Configs.MIN_SDK

                testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = Configs.javaVersion
                targetCompatibility = Configs.javaVersion
            }

            kotlinOptions {
                jvmTarget = Configs.jvmTarget
            }

            buildFeatures {
                compose = true
            }
        }
    }

    /**
     * Configures the [android][com.android.build.gradle.LibraryExtension] extension.
     */
    private fun Project.android(configure: Action<LibraryExtension>): Unit =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("android", configure)

    private fun LibraryExtension.kotlinOptions(configure: Action<KotlinJvmOptions>): Unit =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure(
            "kotlinOptions",
            configure,
        )
}
