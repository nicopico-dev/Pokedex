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
        id = "versioning"
        implementationClass = "fr.nicopico.gradle.version.VersioningPlugin"
    }
}