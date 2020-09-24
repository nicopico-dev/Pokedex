package fr.nicopico.gradle.keystoreconfig.internal

import java.io.File

internal class EnvironmentBackendSigningConfig(
    name: String,
    private val variableNames: VariableNames,
    private val envGetter: EnvironmentGetter,
    private val fileFinder: FileFinder
) : SigningConfigBase(name) {

    override fun getStoreFile(): File =
        extractValue { storeFile }.let(fileFinder)

    override fun getStorePassword(): String =
        extractValue { storePassword }

    override fun getKeyAlias(): String =
        extractValue { keyAlias }

    override fun getKeyPassword(): String =
        extractValue { keyPassword }

    private fun extractValue(what: VariableNames.() -> String): String {
        val variableName = variableNames.what()
        return envGetter(variableName)
            ?: error("Environment variable $variableName is not defined")
    }

    data class VariableNames(
        val storeFile: String,
        val storePassword: String,
        val keyAlias: String,
        val keyPassword: String
    )
}
