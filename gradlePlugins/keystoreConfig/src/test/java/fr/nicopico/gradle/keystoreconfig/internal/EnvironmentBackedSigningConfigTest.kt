package fr.nicopico.gradle.keystoreconfig.internal

import com.android.builder.model.SigningConfig
import com.google.common.truth.Truth.assertThat
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

    private fun createSigningConfig(variableNames: VariableNames): SigningConfig = EnvironmentBackendSigningConfig(
        "signingConfig",
        variableNames,
        envGetter,
        fileFinder
    )

    @Test
    fun `Retrieve relevant information from the environment variables`() {
        // Given
        val variableNames = VariableNames()
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
        assertThat(signingConfig.isSigningReady).isTrue()
    }

    @Test
    fun `Retrieve relevant information from the environment variables (custom names)`() {
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
        assertThat(signingConfig.isSigningReady).isTrue()
    }

    @Test
    fun `Mark signing config as not-ready if an environment variable is not set`() {
        // Given
        val variableNames = VariableNames()
        every { envGetter.invoke(variableNames.storeFile) } returns "keystores/some.keystore"
        every { envGetter.invoke(variableNames.storePassword) } returns "store password"
        every { envGetter.invoke(variableNames.keyAlias) } returns null
        every { envGetter.invoke(variableNames.keyPassword) } returns "key password"

        // When
        val signingConfig = createSigningConfig(variableNames)

        // Then
        assertThat(signingConfig.isSigningReady).isFalse()
    }
}
