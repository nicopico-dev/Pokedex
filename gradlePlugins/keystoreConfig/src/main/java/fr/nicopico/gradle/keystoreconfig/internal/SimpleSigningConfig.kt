package fr.nicopico.gradle.keystoreconfig.internal

import java.io.File

internal class SimpleSigningConfig(
    name: String,
    private val storeFile: File,
    private val storePassword: String,
    private val keyAlias: String,
    private val keyPassword: String
) : SigningConfigBase(name) {

    override fun getStoreFile(): File = storeFile

    override fun getStorePassword(): String = storePassword

    override fun getKeyAlias(): String = keyAlias

    override fun getKeyPassword(): String = keyPassword
}
