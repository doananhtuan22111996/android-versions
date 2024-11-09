package vn.core.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension

class AndroidPublishingPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.apply("org.gradle.maven-publish")

        android {
            publishing {
                multipleVariants("all") {
                    allVariants()
                    withSourcesJar()
                }
            }
        }

        publishing {
            repositories {
                maven {
                    name = "Packages"
                    credentials {
                        username = ghUsername
                        password = ghPassword
                    }
                }
            }
        }
    }

    /**
     * Configures the [android][com.android.build.gradle.LibraryExtension] extension.
     */
    private fun Project.android(configure: Action<LibraryExtension>): Unit =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("android", configure)

    private fun Project.publishing(block: PublishingExtension.() -> Unit) {
        extensions.configure<PublishingExtension>("publishing") {
            apply(block)
        }
    }
}
