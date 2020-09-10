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

    companion object {
        private val pattern = Regex("(\\d+)\\.(\\d+)(?:\\.(\\d+))?.*")

        @Throws(IllegalArgumentException::class)
        internal fun parse(value: String): Version {
            pattern.matchEntire(value)?.let {
                val major = it.groups[1]!!.value.toInt()
                val minor = it.groups[2]!!.value.toInt()
                val patch = it.groups[3]?.value?.toInt() ?: 0

                return Version(major, minor, patch, 0)

            } ?: throw IllegalArgumentException("value is not a valid Version")
        }
    }
}
