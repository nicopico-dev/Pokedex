package fr.nicopico.gradle.keystoreconfig.internal

import java.security.KeyStore
import com.android.build.api.dsl.SigningConfig as SigningConfigDsl

@Suppress("UnstableApiUsage")
internal abstract class SigningConfigBase(
    private val name: String
) : SigningConfigDsl {

    override fun getName(): String = name
    override var storeType: String? = KeyStore.getDefaultType()
    override var isV1SigningEnabled: Boolean = true
    override var isV2SigningEnabled: Boolean = true
}
