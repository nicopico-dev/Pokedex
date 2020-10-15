package fr.nicopico.gradle.version

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

open class PrintVersionTask : DefaultTask() {

    @Input
    val version: Property<Version> = project.objects.property()

    @TaskAction
    fun printVersion() {
        val currentVersion = version.get()
        println(currentVersion.versionName)
    }
}
