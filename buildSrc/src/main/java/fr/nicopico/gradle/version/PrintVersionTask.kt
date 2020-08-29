package fr.nicopico.gradle.version

import fr.nicopico.gradle.version.internal.VersionFileHandler
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

open class PrintVersionTask : DefaultTask() {

    @Suppress("UnstableApiUsage")
    @InputFile
    val versionFile: RegularFileProperty = project.objects.fileProperty()

    @Internal
    val version: Provider<Version> = versionFile.map {
        VersionFileHandler.readVersion(it.asFile)
    }

    @TaskAction
    fun updateVersion() {
        val currentVersion = version.get()
        println(currentVersion.versionName)
    }
}
