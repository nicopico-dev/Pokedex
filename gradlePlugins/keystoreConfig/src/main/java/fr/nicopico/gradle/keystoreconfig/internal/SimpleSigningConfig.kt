package fr.nicopico.gradle.keystoreconfig.internal

import java.io.File

internal class SimpleSigningConfig(
    name: String,
    override var storeFile: File?,
    override var storePassword: String?,
    override var keyAlias: String?,
    override var keyPassword: String?
) : SigningConfigBase(name)
