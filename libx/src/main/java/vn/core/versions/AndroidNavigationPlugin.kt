package vn.core.versions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libx.libs

class AndroidNavigationPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        dependencies {
            catalog {
                implementation(bundle(libs.bundles.navigationComponents))
            }
        }
    }
}
