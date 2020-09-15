package fr.nicopico.gradle.keystoreconfig

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class KeystoreConfigPluginTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()
    lateinit var buildFile: File

    @Before
    fun setUp() {
        buildFile = testProjectDir.newFile("build.gradle")
        buildFile.writeText("""
            plugins {
                id 'fr.nicopico.gradle.keystoreconfig'
            }
            
        """.trimIndent())
    }

    @Test
    fun `test plugin`() {
        // Given
        val taskName = "someTask"

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments(taskName)
            .build()

        // Then
        assertThat(result.task(":$taskName")?.outcome)
            .isIn(listOf(TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE))
    }
}
