plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.palantir.idea-test-fix") version "0.1.0"
}

repositories {
    mavenCentral()
    google()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

gradlePlugin {
    plugins.register("keystores") {
        id = "fr.nicopico.gradle.keystores"
        implementationClass = "fr.nicopico.gradle.keystoreconfig.KeystoreConfigPlugin"
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle:4.2.0")

    testImplementation("junit:junit:4.13.1")
    testImplementation("io.mockk:mockk:1.10.2")
    testImplementation("com.google.truth:truth:1.1")
}
