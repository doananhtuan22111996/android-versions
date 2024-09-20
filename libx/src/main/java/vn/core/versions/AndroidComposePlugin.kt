package vn.core.versions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import vn.core.libx.mobilex

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        dependencies {
            catalog {
                implementation(bundle(mobilex.bundles.jetpackComposeComponents))
            }
        }
    }
}
