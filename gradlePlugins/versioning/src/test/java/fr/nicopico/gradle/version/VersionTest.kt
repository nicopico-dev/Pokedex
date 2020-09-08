package fr.nicopico.gradle.version

import com.google.common.truth.Truth.assertThat
import fr.nicopico.gradle.version.internal.bumpMajor
import fr.nicopico.gradle.version.internal.bumpMinor
import fr.nicopico.gradle.version.internal.bumpPatch
import fr.nicopico.gradle.version.internal.incrementBuild
import org.junit.Test

class VersionTest {

    @Test
    fun `bumpMajor() reset all other parts except build`() {
        // Given
        val baseVersion = Version(1, 2, 3, 41)

        // When
        val actual = baseVersion.bumpMajor()

        // Then
        assertThat(actual).isEqualTo(Version(2, 0, 0, 41))
    }

    @Test
    fun `bumpMinor() reset patch only`() {
        // Given
        val baseVersion = Version(1, 2, 3, 41)

        // When
        val actual = baseVersion.bumpMinor()

        // Then
        assertThat(actual).isEqualTo(Version(1, 3, 0, 41))
    }

    @Test
    fun `bumpPatch() reset nothing`() {
        // Given
        val baseVersion = Version(1, 2, 3, 41)

        // When
        val actual = baseVersion.bumpPatch()

        // Then
        assertThat(actual).isEqualTo(Version(1, 2, 4, 41))
    }

    @Test
    fun `incrementBuild() only change the build number`() {
        // Given
        val baseVersion = Version(1, 2, 3, 41)

        // When
        val actual = baseVersion.incrementBuild()

        // Then
        assertThat(actual).isEqualTo(Version(1, 2, 3, 42))
    }

    @Test
    fun `versionCodes are ordered correctly`() {
        // Given
        val baseVersion = Version(1, 2, 3, 41)

        // When
        val major = baseVersion.bumpMajor()
        val minor = baseVersion.bumpMinor()
        val patch = baseVersion.bumpPatch()

        // Then
        assertThat(major.versionCode).isGreaterThan(minor.versionCode)
        assertThat(minor.versionCode).isGreaterThan(patch.versionCode)
    }

    @Test
    fun `build number has no influence on versionCode, only the versionName`() {
        // Given
        val baseVersion = Version(1, 2, 3, 41)

        // When
        val build = baseVersion.incrementBuild()

        // Then
        assertThat(build.versionCode).isEqualTo(baseVersion.versionCode)
        assertThat(build.versionName).isNotEqualTo(baseVersion.versionName)
    }
}
