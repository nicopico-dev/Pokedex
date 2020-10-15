package fr.nicopico.gradle.version

import com.google.common.truth.Truth.assertThat
import fr.nicopico.gradle.version.internal.bumpMajor
import fr.nicopico.gradle.version.internal.bumpMinor
import fr.nicopico.gradle.version.internal.bumpPatch
import org.junit.Test

class VersionTest {

    @Test
    fun `versionName contains all 3 parts`() {
        // Given
        val version = Version(1, 2, 3)

        // When
        val versionName = version.versionName

        // Then
        assertThat(versionName).isEqualTo("1.2.3")
    }

    @Test
    fun `version suffix is appended at the end of the versionName`() {
        // Given
        val version = Version(1, 2, 3, suffix = "rc")

        // When
        val versionName = version.versionName

        // Then
        assertThat(versionName).isEqualTo("1.2.3rc")
    }

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

    @Test
    fun `versionCodes is not influenced by versionCode`() {
        // Given
        val baseVersion = Version(1, 2, 3)

        // When
        val withSuffix = baseVersion.copy(suffix = "rc")

        // Then
        assertThat(baseVersion.versionCode).isEqualTo(withSuffix.versionCode)
    }
}
