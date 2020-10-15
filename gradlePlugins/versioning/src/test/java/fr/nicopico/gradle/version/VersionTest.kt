package fr.nicopico.gradle.version

import com.google.common.truth.Truth.assertThat
import fr.nicopico.gradle.version.internal.bumpMajor
import fr.nicopico.gradle.version.internal.bumpMinor
import fr.nicopico.gradle.version.internal.bumpPatch
import org.junit.Test

class VersionTest {

    @Test
    fun `bumpMajor() reset all other parts except build`() {
        // Given
        val baseVersion = Version(1, 2, 3)

        // When
        val actual = baseVersion.bumpMajor()

        // Then
        assertThat(actual).isEqualTo(Version(2, 0, 0))
    }

    @Test
    fun `bumpMinor() reset patch only`() {
        // Given
        val baseVersion = Version(1, 2, 3)

        // When
        val actual = baseVersion.bumpMinor()

        // Then
        assertThat(actual).isEqualTo(Version(1, 3, 0))
    }

    @Test
    fun `bumpPatch() reset nothing`() {
        // Given
        val baseVersion = Version(1, 2, 3)

        // When
        val actual = baseVersion.bumpPatch()

        // Then
        assertThat(actual).isEqualTo(Version(1, 2, 4))
    }

    @Test
    fun `versionCodes are ordered correctly`() {
        // Given
        val baseVersion = Version(1, 2, 3)

        // When
        val major = baseVersion.bumpMajor()
        val minor = baseVersion.bumpMinor()
        val patch = baseVersion.bumpPatch()

        // Then
        assertThat(major.versionCode).isGreaterThan(minor.versionCode)
        assertThat(minor.versionCode).isGreaterThan(patch.versionCode)
    }
}
