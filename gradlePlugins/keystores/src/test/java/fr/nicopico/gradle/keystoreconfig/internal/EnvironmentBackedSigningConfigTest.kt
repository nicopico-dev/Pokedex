package fr.nicopico.gradle.keystoreconfig.internal

import com.google.common.truth.Truth.assertThat
import fr.nicopico.gradle.keystoreconfig.accessAnyField
import fr.nicopico.gradle.keystoreconfig.internal.EnvironmentBackendSigningConfig.VariableNames
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@Suppress("UnstableApiUsage")
class EnvironmentBackedSigningConfigTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    @MockK
    private lateinit var fileFinder: FileFinder
    @MockK
    private lateinit var envGetter: EnvironmentGetter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val filePathSlot = slot<String>()
        every { fileFinder.invoke(capture(filePathSlot)) } answers
                { File(testProjectDir.root, filePathSlot.captured) }
    }

    private fun createSigningConfig(variableNames: VariableNames) = EnvironmentBackendSigningConfig(
        "signingConfig",
        variableNames,
        envGetter,
        fileFinder
    )

    @Test
    fun `Retrieve relevant information from the environment variables`() {
        // Given
        val variableNames = VariableNames("A", "B", "C", "D")
        every { envGetter.invoke(variableNames.storeFile) } returns "keystores/some.keystore"
        every { envGetter.invoke(variableNames.storePassword) } returns "store password"
        every { envGetter.invoke(variableNames.keyAlias) } returns "key alias"
        every { envGetter.invoke(variableNames.keyPassword) } returns "key password"

        // When
        val signingConfig = createSigningConfig(variableNames)

        // Then
        assertThat(signingConfig.storeFile).isEqualTo(File(testProjectDir.root, "keystores/some.keystore"))
        assertThat(signingConfig.storePassword).isEqualTo("store password")
        assertThat(signingConfig.keyAlias).isEqualTo("key alias")
        assertThat(signingConfig.keyPassword).isEqualTo("key password")
    }

    @Test
    fun `Fail on access if the environment variable is not set`() {
        // Given
        val variableNames = VariableNames("A", "B", "C", "D")
        every { envGetter.invoke(variableNames.storeFile) } returns "keystores/some.keystore"
        every { envGetter.invoke(variableNames.storePassword) } returns "store password"
        every { envGetter.invoke(variableNames.keyAlias) } returns null
        every { envGetter.invoke(variableNames.keyPassword) } returns "key password"

        // When
        val signingConfig = createSigningConfig(variableNames)
        var error: Exception? = null
        try {
            signingConfig.accessAnyField()
        } catch (e: Exception) {
            error = e
        }

        // Then
        assertThat(error).isInstanceOf(IllegalStateException::class.java)
    }
}
