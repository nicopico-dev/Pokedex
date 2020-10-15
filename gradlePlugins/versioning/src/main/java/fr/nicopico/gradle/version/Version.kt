package fr.nicopico.gradle.version

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val suffix: String = ""
) {
    val versionName: String = "$major.$minor.$patch$suffix"

    @Suppress("MagicNumber")
    val versionCode: Int = major * 10_000 +
            minor * 100 +
            patch

    companion object {
        private val pattern = Regex("(\\d+)\\.(\\d+)(?:\\.(\\d+))?(.+)?")

        private const val MAJOR_INDEX = 1
        private const val MINOR_INDEX = 2
        private const val PATCH_INDEX = 3
        private const val SUFFIX_INDEX = 4

        @Throws(IllegalArgumentException::class)
        internal fun parse(value: String): Version {
            pattern.matchEntire(value)?.let {
                val major = it.groups[MAJOR_INDEX]!!.value.toInt()
                val minor = it.groups[MINOR_INDEX]!!.value.toInt()
                val patch = it.groups[PATCH_INDEX]?.value?.toInt() ?: 0
                val suffix = it.groups[SUFFIX_INDEX]?.value ?: ""

                return Version(major, minor, patch, suffix)

            } ?: throw IllegalArgumentException("value is not a valid Version")
        }
    }
}
