package vn.core.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import vn.core.libs.mobilex

class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.run {
            apply(mobilex.plugins.androidLibrary)
            apply(mobilex.plugins.kotlinAndroid)
            apply(mobilex.plugins.androidHilt)
            apply("kotlin-kapt")
        }

        androidConfig()

        dependencies {
            catalog {
                implementation(bundle(mobilex.bundles.coreAndroidComponents))
                implementation(bundle(mobilex.bundles.pagingComponents))
                implementation(get(mobilex.androidxRoomRuntime))
                kapt(get(mobilex.androidxRoomCompiler))
                implementation(get(mobilex.androidxSecurity))
                implementation(get(mobilex.androidxHilt))
                kapt(get(mobilex.androidxHiltCompiler))
                implementation(get(mobilex.retrofit))
                implementation(get(mobilex.retrofitGson))
                implementation(get(mobilex.loggerTimber))
                implementation(get(mobilex.loggerOkhttp))
                implementation(get(mobilex.coilNetwork))
                implementation(get(mobilex.coilCompose))
                testImplementation(bundle(mobilex.bundles.testComponents))
                androidTestImplementation(bundle(mobilex.bundles.androidTestComponents))
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
                dataBinding = true
                viewBinding = true
                buildConfig = true
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
