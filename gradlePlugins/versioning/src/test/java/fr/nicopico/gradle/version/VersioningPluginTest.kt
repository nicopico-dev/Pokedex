package fr.nicopico.gradle.version

import com.google.common.truth.Truth.assertThat
import fr.nicopico.gradle.version.internal.VersionFileHandler
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class VersioningPluginTest {

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
    fun `printVersion will fail if property file is found`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printVersion")
            .buildAndFail()

        // Then
        assertThat(result.task(":printVersion")?.outcome).isEqualTo(TaskOutcome.FAILED)
    }

    @Test
    fun `printVersion will use "version|properties" by default and print the versionName`() {
        // Given
        val propertyFile = testProjectDir.newFile("version.properties")
        val version = Version(1, 2, 3, 42)
        VersionFileHandler.writeVersion(propertyFile, version)

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printVersion")
            .build()

        // Then
        assertThat(result.task(":printVersion")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains(version.versionName)
    }

    @Test
    fun `printVersion will use the file specified and print the versionName`() {
        // Given
        val propertyFile = testProjectDir.newFile("something.properties")
        val version = Version(1, 2, 3, 42)
        VersionFileHandler.writeVersion(propertyFile, version)

        buildFile.appendText("""
            versioning {
                versionFile 'something.properties'
            }
            
        """.trimIndent())

        print("buildFile:\n" + buildFile.readText())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printVersion")
            .build()

        // Then
        assertThat(result.task(":printVersion")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains(version.versionName)
    }
}