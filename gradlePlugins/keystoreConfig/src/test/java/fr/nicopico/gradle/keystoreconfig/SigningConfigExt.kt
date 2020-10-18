package fr.nicopico.gradle.keystoreconfig

import com.android.builder.model.SigningConfig

fun SigningConfig.accessAnyField() {
    // Access any property to load the properties data
    this.isSigningReady
}
