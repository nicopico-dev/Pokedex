package fr.nicopico.gradle.keystoreconfig

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class KeystoreConfigPluginIntegrationTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()
    lateinit var buildFile: File

    @Before
    fun setUp() {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @Test
    fun `SigningConfigs can be passed to android buildTypes`() {
        // Given
        testProjectDir.newFolder("keystores")
        testProjectDir.newFile("keystores/debug.keystore")

        val configFile = testProjectDir.newFile("debug_keystore.properties")

        configFile.writeText("""
            STORE_FILE=keystores/debug.keystore
            STORE_PASSWORD=store password
            KEY_ALIAS=key alias
            KEY_PASSWORD=key password
        """.trimIndent())

        buildFile.writeText("""
            plugins {
                id 'com.android.application'
                id 'fr.nicopico.gradle.keystores'
            }
            
            keystores {
                debug {
                    configFile 'debug_keystore.properties'
                }
            }
            
            android {
                defaultConfig {
                    applicationId 'fr.nicopico.test'
                    compileSdkVersion 21
                }
            
                buildTypes {
                    debug {
                        signingConfig keystores.debug.signingConfig
                    }
                }
            }
            
            task stubTask {
                doLast {
                    println("OK!")
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments(":stubTask")
            .build()

        // Then
        assertThat(result.task(":stubTask")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}
