package fr.nicopico.gradle.version

import com.google.common.truth.Truth
import fr.nicopico.gradle.version.internal.*
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File

@RunWith(Parameterized::class)
class VersioningPluginBumpTest(
    private val taskName: String,
    private val updatedVersion: Version
) {

    companion object {
        private val version = Version(1, 2, 3, 41)

        @JvmStatic
        @Parameters(name = "task {0}")
        fun parameters() = listOf(
            arrayOf("bumpMajor", version.bumpMajor()),
            arrayOf("bumpMinor", version.bumpMinor()),
            arrayOf("bumpPatch", version.bumpPatch()),
            arrayOf("incrementBuild", version.incrementBuild())
        )
    }

    @get:Rule
    val testProjectDir = TemporaryFolder()
    lateinit var buildFile: File

    @Before
    fun setUp() {
        buildFile = testProjectDir.newFile("build.gradle")
        buildFile.writeText("""
            plugins {
                id 'fr.nicopico.gradle.versioning'
            }
            
        """.trimIndent())
    }

    @Test
    fun `update the version file correctly`() {
        // Given
        // Note: we cannot use mocks as GradleRunner is running the project on a different classpath
        val versionFile = testProjectDir.newFile("version.properties")
        VersionFileHandler.writeVersion(versionFile, version)

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments(taskName)
            .build()

        // Then
        Truth.assertThat(result.task(":$taskName")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        Truth.assertThat(VersionFileHandler.readVersion(versionFile)).isEqualTo(updatedVersion)
    }

    @Test
    fun `update the provided version file correctly`() {
        // Given
        // Note: we cannot use mocks as GradleRunner is running the project on a different classpath
        val versionFile = testProjectDir.newFile("something.properties")
        VersionFileHandler.writeVersion(versionFile, version)

        buildFile.appendText("""
            versioning {
                versionFile 'something.properties'
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments(taskName)
            .build()

        // Then
        Truth.assertThat(result.task(":$taskName")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        Truth.assertThat(VersionFileHandler.readVersion(versionFile)).isEqualTo(updatedVersion)
    }

    @Test
    fun `fail if the version file does not exist`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments(taskName)
            .buildAndFail()

        // Then
        Truth.assertThat(result.task(":$taskName")?.outcome).isEqualTo(TaskOutcome.FAILED)
    }
}
