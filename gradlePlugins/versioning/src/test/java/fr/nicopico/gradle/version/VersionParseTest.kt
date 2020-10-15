package fr.nicopico.gradle.version

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class VersionParseTest(
    private val versionName: String,
    private val expectedVersion: Version?
) {

    companion object {
        @Parameters(name = "Parsing {0} should give {1}")
        @JvmStatic
        fun parameters() = listOf(
            arrayOf("0.0.0", Version(0, 0, 0)),
            arrayOf("1.2.3", Version(1, 2, 3)),
            arrayOf("10.200.3000", Version(10, 200, 3000)),
            arrayOf("3.0.1", Version(3, 0, 1)),
            arrayOf("3.0.1b", Version(3, 0, 1)),
            arrayOf("4.1", Version(4, 1, 0)),
            arrayOf("4.1-rc", Version(4, 1, 0)),
            arrayOf("not-a-version", null),
            arrayOf("abc 1.2.3", null)
        )
    }

    @Test
    fun test() {
        val parsedVersion = try {
            Version.parse(versionName)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }

        assertThat(parsedVersion).isEqualTo(expectedVersion)
    }
}
