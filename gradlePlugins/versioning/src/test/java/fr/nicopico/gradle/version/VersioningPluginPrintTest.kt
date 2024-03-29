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

class VersioningPluginPrintTest {

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
    fun `printVersion will fail if default property file is not found`() {
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
        val version = Version(1, 2, 3)
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
    fun `printVersion will use the file specified by path and print the versionName`() {
        // Given
        val propertyFile = testProjectDir.newFile("something.properties")
        val version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        buildFile.appendText("""
            versioning {
                versionFile 'something.properties'
            }
            
        """.trimIndent())

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
        val version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        buildFile.appendText("""
            versioning {
                versionFile file('something.properties')
            }
            
        """.trimIndent())

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
    fun `printVersion will fail if specified property file is not found`() {
        // Given
        buildFile.appendText("""
            versioning {
                versionFile 'something.properties'
            }
            
        """.trimIndent())

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
    fun `printVersion will use the suffix provided by the plugin configuration`() {
        // Given
        val propertyFile = testProjectDir.newFile("version.properties")
        val version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        buildFile.appendText("""
            versioning {
                versionSuffix 'rc'
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printVersion")
            .build()

        // Then
        val versionWithSuffix = version.copy(suffix = "rc")
        assertThat(result.task(":printVersion")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains(versionWithSuffix.versionName)
    }

    @Test
    fun `printVersion will use the suffix provided by the command line`() {
        // Given
        val propertyFile = testProjectDir.newFile("version.properties")
        val version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printVersion", "-PversionSuffix=rc")
            .build()

        // Then
        val versionWithSuffix = version.copy(suffix = "rc")
        assertThat(result.task(":printVersion")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains(versionWithSuffix.versionName)
    }

    @Test
    fun `command-line suffix takes priority over configuration suffix`() {
        // Given
        val propertyFile = testProjectDir.newFile("version.properties")
        val version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        buildFile.appendText("""
            versioning {
                versionSuffix 'config'
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printVersion", "-PversionSuffix=rc")
            .withDebug(true)
            .build()

        // Then
        val versionWithSuffix = version.copy(suffix = "rc")
        assertThat(result.task(":printVersion")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains(versionWithSuffix.versionName)
    }
}
