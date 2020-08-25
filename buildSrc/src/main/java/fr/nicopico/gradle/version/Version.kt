package fr.nicopico.gradle.version

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val build: Int
) {
    val versionName: String = "$major.$minor.$patch ($build)"

    @Suppress("MagicNumber")
    val versionCode: Int = major * 10_000 +
            minor * 100 +
            patch
}
