package fr.nicopico.gradle.keystoreconfig.internal

import com.android.builder.model.SigningConfig
import java.security.KeyStore

internal abstract class SigningConfigBase(
    private val name: String
) : SigningConfig {

    override fun getName(): String = name

    override fun getStoreType(): String = KeyStore.getDefaultType()
    override fun isV1SigningEnabled(): Boolean = true
    override fun isV2SigningEnabled(): Boolean = true
    override fun isSigningReady(): Boolean = storeFile != null
            && storePassword != null
            && keyAlias != null
            && keyPassword != null
}
