package fr.nicopico.gradle.keystoreconfig

import org.gradle.api.Plugin
import org.gradle.api.Project

class KeystoreConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val configContainer = target.container(KeystoreConfig::class.java) { name ->
            KeystoreConfig(name, target)
        }

        target.extensions.add("keystoreConfigs", configContainer)
    }
}
