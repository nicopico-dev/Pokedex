plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
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
