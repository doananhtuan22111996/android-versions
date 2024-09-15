package vn.core.versions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libx.libs

class AndroidFirebasePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.apply {
            if (isAndroidApplication) {
                apply(libs.plugins.googleService)
                apply(libs.plugins.firebaseCrashlytics)
            }
        }
        dependencies {
            catalog {
                implementation(bundle(libs.bundles.firebaseComponents))
            }
        }
    }
}
