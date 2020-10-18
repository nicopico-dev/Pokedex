package fr.nicopico.gradle.keystoreconfig.internal

import java.io.File

@Suppress("UnstableApiUsage")
internal class EnvironmentBackendSigningConfig(
    name: String,
    private val variableNames: VariableNames,
    private val envGetter: EnvironmentGetter,
    private val fileFinder: FileFinder
) : SigningConfigBase(name) {

    override var storeFile: File?
        get() = extractValue { storeFile }.let(fileFinder)
        set(_) { throw UnsupportedOperationException("Read-only property") }

    override var storePassword: String?
        get() = extractValue { storePassword }
        set(_) { throw UnsupportedOperationException("Read-only property") }

    override var keyAlias: String?
        get() = extractValue { keyAlias }
        set(_) { throw UnsupportedOperationException("Read-only property") }

    override var keyPassword: String?
        get() = extractValue { keyPassword }
        set(_) { throw UnsupportedOperationException("Read-only property") }

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
