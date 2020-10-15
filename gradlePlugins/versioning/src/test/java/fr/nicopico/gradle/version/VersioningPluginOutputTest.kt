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

class VersioningPluginOutputTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()
    lateinit var buildFile: File

    lateinit var version: Version

    @Before
    fun setUp() {
        val propertyFile = testProjectDir.newFile("version.properties")
        version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        buildFile = testProjectDir.newFile("build.gradle")
        buildFile.writeText("""
            plugins {
                id 'fr.nicopico.gradle.versioning'
            }
            
            // Create a task to print versioning extension properties
            task checkVersioningOutput {
                doLast {
                    System.out.println("versionCode " + versioning.versionCode)
                    System.out.println("versionName " + versioning.versionName)
                }
            }
            
        """.trimIndent())
    }

    @Test
    fun `extension will contains version information after configuration`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("checkVersioningOutput")
            .build()

        // Then
        assertThat(result.task(":checkVersioningOutput")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("versionCode " + version.versionCode)
        assertThat(result.output).contains("versionName " + version.versionName)
    }

    @Test
    fun `extension will use the suffix provided by the plugin configuration`() {
        // Given
        buildFile.appendText("""
            versioning {
                versionSuffix 'rc'
            }
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("checkVersioningOutput")
            .build()

        // Then
        val withSuffix = version.copy(suffix = "rc")
        assertThat(result.task(":checkVersioningOutput")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("versionCode " + withSuffix.versionCode)
        assertThat(result.output).contains("versionName " + withSuffix.versionName)
    }

    @Test
    fun `extension will use the suffix provided by the command line`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("checkVersioningOutput", "-PversionSuffix=rc")
            .build()

        // Then
        val withSuffix = version.copy(suffix = "rc")
        assertThat(result.task(":checkVersioningOutput")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("versionCode " + withSuffix.versionCode)
        assertThat(result.output).contains("versionName " + withSuffix.versionName)
    }

    @Test
    fun `command-line suffix takes priority over configuration suffix`() {
        // Given
        buildFile.appendText("""
            versioning {
                versionSuffix 'config'
            }
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("checkVersioningOutput", "-PversionSuffix=rc")
            .build()

        // Then
        val withSuffix = version.copy(suffix = "rc")
        assertThat(result.task(":checkVersioningOutput")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("versionCode " + withSuffix.versionCode)
        assertThat(result.output).contains("versionName " + withSuffix.versionName)
    }
}
