package fr.nicopico.gradle.keystoreconfig.internal

import com.android.builder.model.SigningConfig
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.FileNotFoundException

class PropertiesBackedSigningConfigTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private val fileFinder: FileFinder = { path ->
        when (path) {
            is File -> path
            is String -> File(testProjectDir.root, path)
            else -> throw UnsupportedOperationException("Unsupported path $path")
        }
    }

    private fun createSigningConfig(propertyFile: File): SigningConfig = PropertiesBackedSigningConfig(
        "signingConfig",
        fileFinder,
        propertyFile
    )

    private fun SigningConfig.accessAnyField() {
        // Access any property to load the properties data
        this.storeFile
    }

    @Test
    fun `Retrieve relevant information from the property file`() {
        // Given
        val keystorePropertyFile = testProjectDir.newFile("keystore.properties").apply {
            writeText("""
                STORE_FILE = keystores/some.keystore
                STORE_PASSWORD = store password 123$%%&33
                KEY_ALIAS = key alias
                KEY_PASSWORD = key password ù*€
            """.trimIndent())
        }

        // When
        val signingConfig = createSigningConfig(keystorePropertyFile)

        // Then
        assertThat(signingConfig.storeFile).isEqualTo(File(testProjectDir.root, "keystores/some.keystore"))
        assertThat(signingConfig.storePassword).isEqualTo("store password 123\$%%&33")
        assertThat(signingConfig.keyAlias).isEqualTo("key alias")
        assertThat(signingConfig.keyPassword).isEqualTo("key password ù*€")
        assertThat(signingConfig.isSigningReady).isTrue()
    }

    @Test
    fun `Fail on access if the file format is invalid`() {
        // Given
        val keystorePropertyFile = testProjectDir.newFile("invalid_keystore.properties").apply {
            writeText("""
                SOME_KEY=value
            """.trimIndent())
        }

        // When
        val signingConfig = createSigningConfig(keystorePropertyFile)
        var error: Exception? = null
        try {
            signingConfig.accessAnyField()
        } catch (e: Exception) {
            error = e
        }

        // Then
        assertThat(error).isInstanceOf(FileFormatException::class.java)
    }

    @Test
    fun `Fail on access if the file does not exists`() {
        // Given

        // When
        val signingConfig = createSigningConfig(File("unknown.properties"))
        var error: Exception? = null
        try {
            signingConfig.accessAnyField()
        } catch (e: Exception) {
            error = e
        }

        // Then
        assertThat(error).isInstanceOf(FileNotFoundException::class.java)
    }
}