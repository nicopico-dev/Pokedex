package fr.nicopico.gradle.keystoreconfig.internal

import java.security.KeyStore
import com.android.build.api.dsl.SigningConfig as SigningConfigDsl
import com.android.build.gradle.internal.dsl.SigningConfig as SigningConfigInternal

/*
 * Gradle Android Plugin 4.1.0 expect [SigningConfigInternal] (implementation)
 * instead of [SigningConfigDsl]
 * TODO Only extends [SigningConfigDsl] once possible
 */
@Suppress("UnstableApiUsage")
internal abstract class SigningConfigBase(
    private val name: String
) : SigningConfigInternal(name), SigningConfigDsl {

    override fun getName(): String = name
    override var storeType: String? = KeyStore.getDefaultType()
    override var isV1SigningEnabled: Boolean = true
    override var isV2SigningEnabled: Boolean = true
}
