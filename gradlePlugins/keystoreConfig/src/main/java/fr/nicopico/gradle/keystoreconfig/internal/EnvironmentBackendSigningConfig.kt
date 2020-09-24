package fr.nicopico.gradle.keystoreconfig.internal

import java.io.File

internal class EnvironmentBackendSigningConfig(
    name: String,
    private val variableNames: VariableNames = VariableNames(),
    private val envGetter: EnvironmentGetter,
    private val fileFinder: FileFinder
) : SigningConfigBase(name) {

    override fun getStoreFile(): File? =
        envGetter(variableNames.storeFile)
            ?.let(fileFinder)

    override fun getStorePassword(): String? =
        envGetter(variableNames.storePassword)

    override fun getKeyAlias(): String? =
        envGetter(variableNames.keyAlias)

    override fun getKeyPassword(): String? =
        envGetter(variableNames.keyPassword)

    data class VariableNames(
        val storeFile: String = "KEYSTORE_FILE",
        val storePassword: String = "KEYSTORE_PASSWORD",
        val keyAlias: String = "KEYSTORE_KEY_ALIAS",
        val keyPassword: String = "KEYSTORE_KEY_PASSWORD"
    )
}
