package fr.nicopico.gradle.keystoreconfig

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task

open class KeystoreConfigPluginExtension {
    // TODO Add plugin configuration here
}

class KeystoreConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions
            .create("keystoreConfig", KeystoreConfigPluginExtension::class.java)

        // TODO modify target project
        target.task("someTask", DefaultTask::class) {
            println("Hello World!")
        }
    }
}
