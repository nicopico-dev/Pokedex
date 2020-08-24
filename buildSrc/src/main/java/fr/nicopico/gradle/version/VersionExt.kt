package fr.nicopico.gradle.version

fun Version.bumpMajor(): Version = copy(major = major + 1, minor = 0, patch = 0)
fun Version.bumpMinor(): Version = copy(minor = minor + 1, patch = 0)
fun Version.bumpPatch(): Version = copy(patch = patch + 1)

fun Version.incrementBuild(): Version = copy(build = build + 1)
