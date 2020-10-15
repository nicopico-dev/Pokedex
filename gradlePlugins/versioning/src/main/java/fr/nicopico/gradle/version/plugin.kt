package fr.nicopico.gradle.version

import fr.nicopico.gradle.version.internal.VersionFileHandler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.task

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class VersioningPluginExtension(
    private val project: Project,
    objects: ObjectFactory
) {

    @Suppress("UnstableApiUsage")
    val versionFile: RegularFileProperty = objects.fileProperty()

    /** Allow easier configuration */
    fun versionFile(file: Any) {
        versionFile.set(project.file(file))
    }

    //region Outputs
    val version: Provider<Version> = versionFile.map {
        VersionFileHandler.readVersion(it.asFile)
    }

    val versionName: String
        get() = version.map { it.versionName }.get()
    val versionCode: Int
        get() = version.map { it.versionCode }.get()
    //endregion
}

private const val TASK_GROUP = "Versioning"

class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions
            .create("versioning", VersioningPluginExtension::class.java, target)

        // Set versionFile default value
        extension.versionFile.convention {
            target.rootProject.file("version.properties")
        }

        target.task("createVersionFile", CreateVersionFileTask::class) {
            group = TASK_GROUP
            description = "Create a property file to hold the version"

            versionFile.set(extension.versionFile)
        }

        target.task("printVersion", PrintVersionTask::class) {
            group = TASK_GROUP
            description = "Print current version"

            versionFile.set(extension.versionFile)
        }

        target.task("bumpMajor", UpdateVersionTask::class) {
            group = TASK_GROUP
            description = "Bump the major version"

            versionPart.set(VersionPart.Major)
            versionFile.set(extension.versionFile)
        }

        target.task("bumpMinor", UpdateVersionTask::class) {
            group = TASK_GROUP
            description = "Bump the minor version"

            versionPart.set(VersionPart.Minor)
            versionFile.set(extension.versionFile)
        }

        target.task("bumpPatch", UpdateVersionTask::class) {
            group = TASK_GROUP
            description = "Bump the patch version"

            versionPart.set(VersionPart.Patch)
            versionFile.set(extension.versionFile)
        }
    }
}
