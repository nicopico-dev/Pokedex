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

class VersioningPluginInitTest {

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
    fun `running createVersionFile task will create a version|properties`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("createVersionFile")
            .build()

        // Then
        assertThat(result.task(":createVersionFile")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        val versionFile = File(testProjectDir.root, "version.properties")
        assertThat(versionFile.exists())

        val version = VersionFileHandler.readVersion(versionFile)
        assertThat(version).isEqualTo(Version(0, 1, 0, 0))
    }

    @Test
    fun `running createVersionFile task will create a version|properties with the provided version`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("createVersionFile", "--initialVersion=1.2.3")
            .build()

        // Then
        assertThat(result.task(":createVersionFile")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        val versionFile = File(testProjectDir.root, "version.properties")
        assertThat(versionFile.exists())

        val version = VersionFileHandler.readVersion(versionFile)
        assertThat(version).isEqualTo(Version(1, 2, 3, 0))
    }

    @Test
    fun `running createVersionFile task will fail is the provided version format is wrong`() {
        // Given

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("createVersionFile", "--initialVersion=something")
            .buildAndFail()

        // Then
        assertThat(result.task(":createVersionFile")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(result.output).contains("provided `initialVersion` is invalid (something)")

        val versionFile = File(testProjectDir.root, "version.properties")
        assertThat(versionFile.exists().not())
    }

    @Test
    fun `running createVersionFile task will create the configured file`() {
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
            .withArguments("createVersionFile")
            .build()

        // Then
        assertThat(result.task(":createVersionFile")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        val versionFile = File(testProjectDir.root, "something.properties")
        assertThat(versionFile.exists())

        val version = VersionFileHandler.readVersion(versionFile)
        assertThat(version).isEqualTo(Version(0, 1, 0, 0))
    }

    @Test
    fun `running createVersionFile task will fail if version|properties file exists`() {
        // Given
        val propertyFile = testProjectDir.newFile("version.properties")
        val version = Version(1, 2, 3, 42)
        VersionFileHandler.writeVersion(propertyFile, version)

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("createVersionFile")
            .buildAndFail()

        // Then
        assertThat(result.task(":createVersionFile")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(result.output).containsMatch("Version file .*$propertyFile already exists")
    }

    @Test
    fun `running createVersionFile task will fail if the provided file exists`() {
        // Given
        val propertyFile = testProjectDir.newFile("something.properties")
        val version = Version(1, 2, 3, 42)
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
            .withArguments("createVersionFile")
            .buildAndFail()

        // Then
        assertThat(result.task(":createVersionFile")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(result.output).containsMatch("Version file .*$propertyFile already exists")
    }
}
