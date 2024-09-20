package vn.core.versions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libx.mobilex

class AndroidFirebasePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.apply {
            if (isAndroidApplication) {
                apply(mobilex.plugins.googleService)
                apply(mobilex.plugins.firebaseCrashlytics)
            }
        }
        dependencies {
            catalog {
                implementation(bundle(mobilex.bundles.firebaseComponents))
            }
        }
    }
}
