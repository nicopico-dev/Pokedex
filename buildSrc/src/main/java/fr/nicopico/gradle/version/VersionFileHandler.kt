package fr.nicopico.gradle.version

import java.io.File
import java.util.*

object VersionFileHandler {
    private const val PROP_MAJOR = "VERSION_MAJOR"
    private const val PROP_MINOR = "VERSION_MINOR"
    private const val PROP_PATCH = "VERSION_PATCH"
    private const val PROP_BUILD = "VERSION_BUILD"

    @JvmStatic
    fun readVersion(versionFile: File): Version {
        val props = Properties().apply {
            load(versionFile.reader(charset = Charsets.UTF_8))
        }

        return Version(
            major = props.getProperty(PROP_MAJOR).toInt(),
            minor = props.getProperty(PROP_MINOR).toInt(),
            patch = props.getProperty(PROP_PATCH).toInt(),
            build = props.getProperty(PROP_BUILD).toInt()
        )
    }

    @JvmStatic
    fun writeVersion(versionFile: File, version: Version) {
        val props = Properties()
        props[PROP_MAJOR] = version.major
        props[PROP_MINOR] = version.minor
        props[PROP_PATCH] = version.patch
        props[PROP_BUILD] = version.build

        props.store(versionFile.writer(charset = Charsets.UTF_8), "Update version")
    }
}