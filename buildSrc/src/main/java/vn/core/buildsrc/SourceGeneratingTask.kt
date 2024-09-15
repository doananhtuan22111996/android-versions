package vn.core.buildsrc

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeAliasSpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import vn.core.buildsrc.Configs.GROUP_ID

abstract class SourceGeneratingTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val outputDirs = outputFile.asFile.get()
        val packageName = GROUP_ID

        val dependencyModelName = "DependencyModel"
        val classBuilder = TypeSpec.classBuilder(dependencyModelName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("libraryAlias", String::class)
                    .addParameter("groupArtifact", String::class)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("libraryAlias", String::class)
                    .initializer("libraryAlias")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("groupArtifact", String::class)
                    .initializer("groupArtifact")
                    .build()
            )
        FileSpec.builder(packageName, dependencyModelName)
            .addType(classBuilder.build())
            .build()
            .writeTo(outputDirs)

        val catalogs = project.extensions.getByType<VersionCatalogsExtension>()
        catalogs.catalogNames.forEach {
            val builder = TypeSpec.objectBuilder(it)
            val catalog = catalogs.named(it)
            catalog.libraryAliases.forEach { alias ->
                val libProvider = catalog.findLibrary(alias)
                if (libProvider.isPresent) {
                    val lib = libProvider.get().get()
                    val className = ClassName(packageName, dependencyModelName)
                    val groupArtifact = "${lib.module.group}:${lib.module.name}"
                    builder.addProperty(
                        PropertySpec.builder(alias, className)
                            .initializer("%T(%S,%S)", className, alias, groupArtifact)
                            .build()
                    )
                }
            }

            val versionAliasBuilder = TypeSpec.objectBuilder("versionAliases")
            catalog.versionAliases.forEach { alias ->
                versionAliasBuilder.addProperty(
                    PropertySpec.builder(alias, String::class)
                        .initializer("%S", alias)
                        .build()
                )
            }
            builder.addType(versionAliasBuilder.build())

            val pluginAliasBuilder = TypeSpec.objectBuilder("plugins")
            catalog.pluginAliases.forEach { alias ->
                val plugin = catalog.findPlugin(alias).get().get()
                pluginAliasBuilder.addProperty(
                    PropertySpec.builder(alias, String::class)
                        .initializer("%S", plugin.pluginId)
                        .build()
                )
            }
            builder.addType(pluginAliasBuilder.build())

            val bundleAliasType = TypeAliasSpec.builder("BundleAlias", String::class).build()
            val bundleAliasesBuilder = TypeSpec.objectBuilder("bundles")
            catalog.bundleAliases.forEach { alias ->
                bundleAliasesBuilder.addProperty(
                    PropertySpec.builder(alias, ClassName(packageName, bundleAliasType.name))
                        .initializer("%S", alias)
                        .build()
                )
            }
            builder.addType(bundleAliasesBuilder.build())
            FileSpec.builder(packageName, it)
                .addTypeAlias(bundleAliasType)
                .addType(builder.build())
                .build()
                .writeTo(outputDirs)
        }
    }
}