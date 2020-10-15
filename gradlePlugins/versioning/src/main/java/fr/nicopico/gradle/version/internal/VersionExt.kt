package fr.nicopico.gradle.version.internal

import fr.nicopico.gradle.version.Version

internal fun Version.bumpMajor(): Version = copy(major = major + 1, minor = 0, patch = 0)
internal fun Version.bumpMinor(): Version = copy(minor = minor + 1, patch = 0)
internal fun Version.bumpPatch(): Version = copy(patch = patch + 1)
