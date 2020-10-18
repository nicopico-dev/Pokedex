package fr.nicopico.gradle.keystoreconfig.internal

import java.security.KeyStore
import com.android.build.gradle.internal.dsl.SigningConfig as SigningConfigDsl
import com.android.builder.model.SigningConfig as SigningConfigInterface

internal abstract class SigningConfigBase(
    private val name: String
) : SigningConfigDsl(name), SigningConfigInterface {

    override fun getName(): String = name

    override fun getStoreType(): String = KeyStore.getDefaultType()
    override fun isV1SigningEnabled(): Boolean = true
    override fun isV2SigningEnabled(): Boolean = true
    override fun isSigningReady(): Boolean = storeFile != null
            && storePassword != null
            && keyAlias != null
            && keyPassword != null
}
