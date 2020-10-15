package fr.nicopico.gradle.version

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int
) {
    val versionName: String = "$major.$minor.$patch"

    @Suppress("MagicNumber")
    val versionCode: Int = major * 10_000 +
            minor * 100 +
            patch

    companion object {
        private val pattern = Regex("(\\d+)\\.(\\d+)(?:\\.(\\d+))?.*")

        private const val MAJOR_INDEX = 1
        private const val MINOR_INDEX = 2
        private const val PATCH_INDEX = 3

        @Throws(IllegalArgumentException::class)
        internal fun parse(value: String): Version {
            pattern.matchEntire(value)?.let {
                val major = it.groups[MAJOR_INDEX]!!.value.toInt()
                val minor = it.groups[MINOR_INDEX]!!.value.toInt()
                val patch = it.groups[PATCH_INDEX]?.value?.toInt() ?: 0

                return Version(major, minor, patch)

            } ?: throw IllegalArgumentException("value is not a valid Version")
        }
    }
}
