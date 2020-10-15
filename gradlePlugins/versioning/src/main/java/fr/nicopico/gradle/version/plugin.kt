package fr.nicopico.gradle.version

import fr.nicopico.gradle.version.internal.VersionFileHandler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.task

private const val TASK_GROUP = "Versioning"
internal const val SUFFIX_PROPERTY = "versionSuffix"

fun extractVersion(regularFile: RegularFile, suffix: String): Version =
    VersionFileHandler.readVersion(regularFile.asFile).copy(suffix = suffix)

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class VersioningPluginExtension(
    private val project: Project,
    objects: ObjectFactory
) {

    @Suppress("UnstableApiUsage")
    val versionFile: RegularFileProperty = objects.fileProperty()
    fun versionFile(file: Any) {
        versionFile.set(project.file(file))
    }

    val versionSuffix: Property<String> = objects.property()
    fun versionSuffix(value: String) {
        versionSuffix.set(value)
    }

    //region Outputs
    @Suppress("UnstableApiUsage")
    val version: Provider<Version> = if (project.hasProperty(SUFFIX_PROPERTY)) {
        // Override configuration with command-line parameter
        versionFile.map { regularFile ->
            extractVersion(regularFile, project.property(SUFFIX_PROPERTY).toString())
        }
    } else {
        // Note: the Provider.zip() method is not found when running tests on Android Studio
        // It works as expected from the command line or when building the plugin
        versionFile.zip(versionSuffix) { regularFile, suffix ->
            extractVersion(regularFile, suffix)
        }
    }

    val versionName: String
        get() = version.map { it.versionName }.get()
    val versionCode: Int
        get() = version.map { it.versionCode }.get()
    //endregion
}

class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions
            .create("versioning", VersioningPluginExtension::class.java, target)

        // Set extension default value
        extension.versionFile.convention {
            target.rootProject.file("version.properties")
        }
        @Suppress("UnstableApiUsage")
        extension.versionSuffix.convention("")

        target.task("createVersionFile", CreateVersionFileTask::class) {
            group = TASK_GROUP
            description = "Create a property file to hold the version"

            versionFile.set(extension.versionFile)
        }

        target.task("printVersion", PrintVersionTask::class) {
            group = TASK_GROUP
            description = "Print current version"

            version.set(extension.version)
        }

        target.task("bumpMajor", UpdateVersionTask::class) {
            group = TASK_GROUP
            description = "Bump the major version"

            versionPart.set(VersionPart.Major)
            versionFile.set(extension.versionFile)
            version.set(extension.version)
        }

        target.task("bumpMinor", UpdateVersionTask::class) {
            group = TASK_GROUP
            description = "Bump the minor version"

            versionPart.set(VersionPart.Minor)
            versionFile.set(extension.versionFile)
            version.set(extension.version)
        }

        target.task("bumpPatch", UpdateVersionTask::class) {
            group = TASK_GROUP
            description = "Bump the patch version"

            versionPart.set(VersionPart.Patch)
            versionFile.set(extension.versionFile)
            version.set(extension.version)
        }
    }
}
