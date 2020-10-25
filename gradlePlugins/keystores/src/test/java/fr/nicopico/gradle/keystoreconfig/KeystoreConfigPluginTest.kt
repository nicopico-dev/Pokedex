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
                id 'fr.nicopico.gradle.keystores'
            }
            
            task printDebugSigningConfig {
                doLast {
                    def signingConfig = keystores.debug.signingConfig
                    println("store file: " + signingConfig.storeFile)
                    println("store password: " + signingConfig.storePassword)
                    println("key alias: " + signingConfig.keyAlias)
                    println("key password: " + signingConfig.keyPassword)
                }
            }
            
        """.trimIndent())
    }

    @Test
    fun `KeystoreConfig are created and populated`() {
        // Given
        buildFile.appendText("""
            keystores {
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
                def debugSigningConfig = keystores.debug.signingConfig
                def releaseSigningConfig = keystores.release.signingConfig
            
                doLast {
                    println("debug: " + (debugSigningConfig != null ? "OK" : "KO"))
                    println("release: " + (releaseSigningConfig != null ? "OK" : "KO"))
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
    fun `KeystoreConfig's signingConfig is populated from the property file (passed as string)`() {
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

        buildFile.appendText("""
            keystores {
                debug {
                    configFile 'debug_keystore.properties'
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .build()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).containsMatch("store file: .*?/keystores/debug\\.keystore")
        assertThat(result.output).contains("store password: store password")
        assertThat(result.output).contains("key alias: key alias")
        assertThat(result.output).contains("key password: key password")
    }

    @Test
    fun `KeystoreConfig's signingConfig is populated from the property file (passed as file)`() {
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

        buildFile.appendText("""
            keystores {
                debug {
                    configFile project.file('debug_keystore.properties')
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .build()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).containsMatch("store file: .*?/keystores/debug\\.keystore")
        assertThat(result.output).contains("store password: store password")
        assertThat(result.output).contains("key alias: key alias")
        assertThat(result.output).contains("key password: key password")
    }

    @Test
    fun `KeystoreConfig fails if the property file does not exists`() {
        // Given
        buildFile.appendText("""
            keystores {
                debug {
                    configFile 'debug_keystore.properties'
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .buildAndFail()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.FAILED)
    }

    @Test
    fun `KeystoreConfig's signingConfig is ok even if the keystore does not exists`() {
        // Given
        val configFile = testProjectDir.newFile("debug_keystore.properties")

        configFile.writeText("""
            STORE_FILE=keystores/debug.keystore
            STORE_PASSWORD=store password
            KEY_ALIAS=key alias
            KEY_PASSWORD=key password
        """.trimIndent())

        buildFile.appendText("""
            keystores {
                debug {
                    configFile 'debug_keystore.properties'
                }
            }
            
        """.trimIndent())

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .build()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).containsMatch("store file: .*?/keystores/debug\\.keystore")
        assertThat(result.output).contains("store password: store password")
        assertThat(result.output).contains("key alias: key alias")
        assertThat(result.output).contains("key password: key password")
    }

    @Test
    fun `KeystoreConfig's signingConfig is populated from environment variables`() {
        // Given
        buildFile.appendText("""
            keystores {
                debug {
                    envVars {
                        storeFile "KEYSTORE_FILE"
                        storePassword "KEYSTORE_PASSWORD"
                        keyAlias "KEYSTORE_KEY_ALIAS"
                        keyPassword "KEYSTORE_KEY_PASSWORD"
                    }
                }
            }
            
        """.trimIndent())

        @Suppress("UnstableApiUsage")
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withEnvironment(mapOf(
                "KEYSTORE_FILE" to "keystores/debug.keystore",
                "KEYSTORE_PASSWORD" to "store password",
                "KEYSTORE_KEY_ALIAS" to "key alias",
                "KEYSTORE_KEY_PASSWORD" to "key password"
            ))
            .withArguments("printDebugSigningConfig")
            .build()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).containsMatch("store file: .*?/keystores/debug\\.keystore")
        assertThat(result.output).contains("store password: store password")
        assertThat(result.output).contains("key alias: key alias")
        assertThat(result.output).contains("key password: key password")
    }

    @Test
    fun `KeystoreConfig fails if the environment variable do not exist`() {
        // Given
        buildFile.appendText("""
            keystores {
                debug {
                    envVars {
                        storeFile "KEYSTORE_FILE"
                        storePassword "KEYSTORE_PASSWORD"
                        keyAlias "KEYSTORE_KEY_ALIAS"
                        keyPassword "KEYSTORE_KEY_PASSWORD"
                    }
                }
            }
            
        """.trimIndent())

        @Suppress("UnstableApiUsage")
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .buildAndFail()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.FAILED)
    }

    @Test
    fun `KeystoreConfig allows to quickly configure a debug keystore (passed as string)`() {
        // Given
        buildFile.appendText("""
            keystores {
                debug {
                    debugKeystore 'keystores/debug.keystore'
                }
            }
            
        """.trimIndent())

        @Suppress("UnstableApiUsage")
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .build()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).containsMatch("store file: .*?/keystores/debug\\.keystore")
        assertThat(result.output).contains("store password: android")
        assertThat(result.output).contains("key alias: androiddebugkey")
        assertThat(result.output).contains("key password: android")
    }

    @Test
    fun `KeystoreConfig allows to quickly configure a debug keystore (passed as file)`() {
        // Given
        buildFile.appendText("""
            keystores {
                debug {
                    debugKeystore project.file('keystores/debug.keystore')
                }
            }
            
        """.trimIndent())

        @Suppress("UnstableApiUsage")
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("printDebugSigningConfig")
            .build()

        // Then
        assertThat(result.task(":printDebugSigningConfig")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).containsMatch("store file: .*?/keystores/debug\\.keystore")
        assertThat(result.output).contains("store password: android")
        assertThat(result.output).contains("key alias: androiddebugkey")
        assertThat(result.output).contains("key password: android")
    }
}
