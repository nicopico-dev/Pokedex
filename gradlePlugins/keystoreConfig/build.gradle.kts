plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.palantir.idea-test-fix") version "0.1.0"
}

repositories {
    jcenter()
    google()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

gradlePlugin {
    plugins.register("keystoreConfig") {
        id = "fr.nicopico.gradle.keystoreconfig"
        implementationClass = "fr.nicopico.gradle.keystoreconfig.KeystoreConfigPlugin"
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:4.0.1")

    testImplementation("junit:junit:4.13")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.google.truth:truth:1.0.1")
}
