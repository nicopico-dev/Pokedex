package fr.nicopico.gradle.version

import fr.nicopico.gradle.version.internal.VersionFileHandler
import fr.nicopico.gradle.version.internal.bumpMajor
import fr.nicopico.gradle.version.internal.bumpMinor
import fr.nicopico.gradle.version.internal.bumpPatch
import fr.nicopico.gradle.version.internal.incrementBuild
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class VersioningPluginExtension {
    var versionFile: Any = "version.properties"

    lateinit var version: Version

    val versionName
        get() = version.versionName
    val versionCode
        get() = version.versionCode
}

private const val TASK_GROUP = "Versioning"

class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("versioning", VersioningPluginExtension::class.java)

        val versionFile: File = target.rootProject.file(extension.versionFile)
        extension.version = VersionFileHandler.readVersion(versionFile)

        target.task("bumpMajor") {
            group = TASK_GROUP
            description = "Bump the major version"

            doLast {
                val version = extension.version
                val newVersion = version.bumpMajor()
                VersionFileHandler.writeVersion(versionFile, newVersion)
            }
        }

        target.task("bumpMinor") {
            group = TASK_GROUP
            description = "Bump the minor version"

            doLast {
                val version = VersionFileHandler.readVersion(versionFile)
                val newVersion = version.bumpMinor()
                VersionFileHandler.writeVersion(versionFile, newVersion)
            }
        }

        target.task("bumpPatch") {
            group = TASK_GROUP
            description = "Bump the patch version"

            doLast {
                val version = VersionFileHandler.readVersion(versionFile)
                val newVersion = version.bumpPatch()
                VersionFileHandler.writeVersion(versionFile, newVersion)
            }
        }

        target.task("incrementBuild") {
            group = TASK_GROUP
            description = "Increment the build number"

            doLast {
                val version = VersionFileHandler.readVersion(versionFile)
                val newVersion = version.incrementBuild()
                VersionFileHandler.writeVersion(versionFile, newVersion)
            }
        }
    }
}
