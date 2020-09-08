package fr.nicopico.gradle.version

import fr.nicopico.gradle.version.internal.*
import fr.nicopico.gradle.version.internal.VersionFileHandler
import fr.nicopico.gradle.version.internal.bumpMajor
import fr.nicopico.gradle.version.internal.bumpMinor
import fr.nicopico.gradle.version.internal.bumpPatch
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.property

open class UpdateVersionTask : DefaultTask() {

    @Suppress("UnstableApiUsage")
    @InputFile
    val versionFile: RegularFileProperty = project.objects.fileProperty()

    @Input
    val versionPart: Property<VersionPart> = project.objects.property()

    @Internal
    val version: Provider<Version> = versionFile.map {
        VersionFileHandler.readVersion(it.asFile)
    }

    @TaskAction
    fun updateVersion() {
        val newVersion = computeNewVersion()
        VersionFileHandler.writeVersion(versionFile.asFile.get(), newVersion)
    }

    private fun computeNewVersion(): Version {
        val currentVersion = version.get()
        return when (versionPart.get()) {
            VersionPart.Major -> currentVersion.bumpMajor()
            VersionPart.Minor -> currentVersion.bumpMinor()
            VersionPart.Patch -> currentVersion.bumpPatch()
            VersionPart.Build -> currentVersion.incrementBuild()
        }
    }
}
