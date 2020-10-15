package fr.nicopico.gradle.version

import fr.nicopico.gradle.version.internal.VersionFileHandler
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class CreateVersionFileTask : DefaultTask() {

    @Suppress("UnstableApiUsage")
    @OutputFile
    val versionFile: RegularFileProperty = project.objects.fileProperty()

    @Option(description = "Configure the initial version to write in the version file. Format: MAJOR.MINOR.PATCH")
    var initialVersion: String? = null

    @TaskAction
    fun createVersionFile() {
        val actualFile = versionFile.get().asFile
        if (actualFile.exists()) {
            throw GradleException("Version file $actualFile already exists")
        } else {
            val version = initialVersion?.let {
                try {
                    Version.parse(it)
                } catch (_: IllegalArgumentException) {
                    throw GradleException("provided `initialVersion` is invalid ($initialVersion)")
                }
            } ?: Version(0, 1, 0)
            VersionFileHandler.writeVersion(actualFile, version)
        }
    }
}
