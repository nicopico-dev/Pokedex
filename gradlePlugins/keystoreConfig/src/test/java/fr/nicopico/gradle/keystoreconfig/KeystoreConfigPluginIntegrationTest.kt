package fr.nicopico.gradle.keystoreconfig

import com.google.common.truth.Truth.assertThat
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

typealias EnvPreparation = (GradleRunner, TemporaryFolder) -> Unit

@RunWith(Parameterized::class)
class KeystoreConfigPluginIntegrationTest(
    private val label: String,
    private val keystoreConfig: String,
    private val envPreparation: EnvPreparation
) {

    @get:Rule
    val testProjectDir = TemporaryFolder()
    lateinit var buildFile: File

    companion object {
        // Array type must be explicit otherwise the compiler fails as if it expected an Array<String>
        @Suppress("RemoveExplicitTypeArguments")
        @Parameters(name = "Keystore config from {0} can be passed to android buildTypes")
        @JvmStatic
        fun parameters() = listOf(
            arrayOf<Any>(
                "properties",
                "configFile 'debug_keystore.properties'",
                { _: GradleRunner, testProjectDir: TemporaryFolder ->
                    val configFile = testProjectDir.newFile("debug_keystore.properties")

                    configFile.writeText("""
                        STORE_FILE=keystores/debug.keystore
                        STORE_PASSWORD=store password
                        KEY_ALIAS=key alias
                        KEY_PASSWORD=key password
                    """.trimIndent())
                    Unit
                }
            ),
            arrayOf<Any>(
                "envVar",
                """
                    envVars {
                        storeFile "KEYSTORE_FILE"
                        storePassword "KEYSTORE_PASSWORD"
                        keyAlias "KEYSTORE_KEY_ALIAS"
                        keyPassword "KEYSTORE_KEY_PASSWORD"
                    }
                """.trimIndent(),
                { gradleEnv: GradleRunner, _: TemporaryFolder ->
                    @Suppress("UnstableApiUsage")
                    gradleEnv.withEnvironment(mapOf(
                        "KEYSTORE_FILE" to "keystores/debug.keystore",
                        "KEYSTORE_PASSWORD" to "store password",
                        "KEYSTORE_KEY_ALIAS" to "key alias",
                        "KEYSTORE_KEY_PASSWORD" to "key password"
                    ))
                    Unit
                }
            ),
            arrayOf<Any>(
                "debugKeystore",
                "debugKeystore project.file('keystores/debug.keystore')",
                { _: GradleRunner, _: TemporaryFolder -> Unit }
            )
        )
    }

    @Before
    fun setUp() {
        buildFile = testProjectDir.newFile("build.gradle")

        testProjectDir.newFolder("keystores")
        testProjectDir.newFile("keystores/debug.keystore")
    }

    @Test
    fun test() {
        // Given
        val gradleEnv = GradleRunner.create()
        envPreparation.invoke(gradleEnv, testProjectDir)

        buildFile.writeText("""
            plugins {
                id 'com.android.application'
                id 'fr.nicopico.gradle.keystores'
            }
            
            keystores {
                debug {
                    $keystoreConfig
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
        val result = gradleEnv
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments(":stubTask")
            .build()

        // Then
        assertThat(result.task(":stubTask")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}
