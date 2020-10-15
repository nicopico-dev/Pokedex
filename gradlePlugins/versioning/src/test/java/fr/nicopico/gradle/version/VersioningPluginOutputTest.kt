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
    fun `extension will contains version information after configuration`() {
        // Given
        val propertyFile = testProjectDir.newFile("version.properties")
        val version = Version(1, 2, 3)
        VersionFileHandler.writeVersion(propertyFile, version)

        // Create a task to print versioning extension properties
        buildFile.appendText("""
            task checkVersioningOutput {
                doLast {
                    System.out.println("versionCode " + versioning.versionCode)
                    System.out.println("versionName " + versioning.versionName)
                }
            }
            
        """.trimIndent())

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
}
