package vn.core.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libs.mobilex
import java.io.File

class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.run {
            apply(mobilex.plugins.androidApplication)
            apply(mobilex.plugins.kotlinAndroid)
            apply(mobilex.plugins.androidHilt)
            apply("kotlin-kapt")
            apply(mobilex.plugins.composeCompiler)
        }

        androidConfig()

        dependencies {
            catalog {
                implementation(bundle(mobilex.bundles.coreAndroidComponents))
                implementation(platform(get(mobilex.androidxComposeBom)))
                implementation(bundle(mobilex.bundles.jetpackComposeComponents))
                implementation(bundle(mobilex.bundles.lifecycleComponents))
                implementation(bundle(mobilex.bundles.navigationComponents))
                implementation(get(mobilex.androidxHilt))
                kapt(get(mobilex.androidxHiltCompiler))
                testImplementation(bundle(mobilex.bundles.testComponents))
                androidTestImplementation(bundle(mobilex.bundles.androidTestComponents))
                androidTestImplementation(bundle(mobilex.bundles.composeTestComponents))
            }
        }
    }

    private fun Project.androidConfig() {
        android {
            compileSdk = Configs.COMPILE_SDK

            defaultConfig {
                minSdk = Configs.MIN_SDK
                targetSdk = Configs.TARGET_SDK
                testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

                vectorDrawables {
                    useSupportLibrary = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }

            compileOptions {
                sourceCompatibility = Configs.javaVersion
                targetCompatibility = Configs.javaVersion
            }

            kotlinOptions {
                jvmTarget = Configs.jvmTarget
            }

            val secretProperties = getSecretPropertyFile(rootProject)
            signingConfigs {
                create(Configs.Mode.RELEASE) {
                    keyAlias = "${secretProperties["SIGNING_KEY_ALIAS"]}"
                    keyPassword = "${secretProperties["SIGNING_KEY_PASSWORD"]}"
                    storePassword = "${secretProperties["SIGNING_KEYSTORE_PASSWORD"]}"
                    storeFile = File("$rootDir/keystore.jks")
                }
            }

            buildTypes {
                getByName(Configs.Mode.DEBUG) {
                    isDebuggable = true
                    isMinifyEnabled = false
                }
                getByName(Configs.Mode.RELEASE) {
                    isDebuggable = false
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
                    )
                    signingConfig = signingConfigs.getByName(Configs.Mode.RELEASE)
                }
            }

            buildFeatures {
                dataBinding = true
                viewBinding = true
                buildConfig = true
                compose = true
            }

            // Allow references to generated code
            kapt {
                correctErrorTypes = true
            }
        }
    }
}
