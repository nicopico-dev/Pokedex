plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.palantir.idea-test-fix") version "0.1.0"
}

repositories {
    jcenter()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

gradlePlugin {
    plugins.register("versioning") {
        id = "fr.nicopico.gradle.versioning"
        implementationClass = "fr.nicopico.gradle.version.VersioningPlugin"
    }
}

dependencies {
    testImplementation("junit:junit:4.13")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.google.truth:truth:1.0.1")
}
