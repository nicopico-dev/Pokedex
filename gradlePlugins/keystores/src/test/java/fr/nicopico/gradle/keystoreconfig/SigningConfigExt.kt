package fr.nicopico.gradle.keystoreconfig

import com.android.build.api.dsl.SigningConfig

@Suppress("UnstableApiUsage")
fun SigningConfig.accessAnyField() {
    // Access any property to load the properties data
    @Suppress("UNUSED_VARIABLE")
    val initialized = this.storeFile != null
            && this.storePassword != null
            && this.keyAlias != null
            && this.keyPassword != null
}
