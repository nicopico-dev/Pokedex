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
    fun `KeystoreConfig are created`() {
        // Given
        buildFile.appendText("""
            keystoreConfigs {
                debug {
                    configFile 'debug_keystore.properties'
                }
                
                release {
                    configFile = 'release_keystore.properties'
                }
            }
            
        """.trimIndent())

        buildFile.appendText("""
            task checkKeystoreConfigOutput {
                doLast {
                    println("debug: " + (keystoreConfigs.debug != null ? "OK" : "KO"))
                    println("release: " + (keystoreConfigs.release != null ? "OK" : "KO"))
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("checkKeystoreConfigOutput")
            .build()

        // Then
        assertThat(result.task(":checkKeystoreConfigOutput")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("debug: OK")
        assertThat(result.output).contains("release: OK")
    }

    @Test
    fun `KeystoreConfig's signingConfigs are populated`() {
        // Given
        buildFile.appendText("""
            keystoreConfigs {
                debug {
                    configFile 'debug_keystore.properties'
                }
                
                release {
                    configFile = 'release_keystore.properties'
                }
            }
            
        """.trimIndent())

        buildFile.appendText("""
            task checkKeystoreConfigOutput {
                doLast {
                    println("debug signingConfig: " + (keystoreConfigs.debug.signingConfig != null ? "OK" : "KO"))
                    println("release signingConfig: " + (keystoreConfigs.release.signingConfig != null ? "OK" : "KO"))
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("checkKeystoreConfigOutput")
            .build()

        // Then
        assertThat(result.task(":checkKeystoreConfigOutput")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("debug signingConfig: OK")
        assertThat(result.output).contains("release signingConfig: OK")
    }
}
