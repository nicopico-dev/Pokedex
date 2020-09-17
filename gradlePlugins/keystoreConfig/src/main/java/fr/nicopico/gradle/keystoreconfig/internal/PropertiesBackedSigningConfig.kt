package fr.nicopico.gradle.keystoreconfig.internal

import com.android.builder.model.SigningConfig
import java.io.File
import java.security.KeyStore
import java.util.*

internal class PropertiesBackedSigningConfig(
    private val name: String,
    private val fileFinder: FileFinder,
    private val propertyFile: File
) : SigningConfig {

    private val props by lazy {
        Properties().apply {
            load(propertyFile.reader(charset = Charsets.UTF_8))
            checkFormat(this)
        }
    }

    @Throws(FileFormatException::class)
    private fun checkFormat(properties: Properties) {
        val missingMandatoryKeys = MANDATORY_KEYS.filter { !properties.containsKey(it) }
        if (missingMandatoryKeys.isNotEmpty()) {
            throw FileFormatException("The following keys are missing in $propertyFile: $missingMandatoryKeys")
        }
    }

    override fun getName(): String = name

    override fun getStoreFile(): File = fileFinder(props.getProperty(PROP_STORE_FILE))

    override fun getStorePassword(): String = props.getProperty(PROP_STORE_PASSWORD)

    override fun getKeyAlias(): String = props.getProperty(PROP_KEY_ALIAS)

    override fun getKeyPassword(): String = props.getProperty(PROP_KEY_PASSWORD)

    override fun getStoreType(): String = KeyStore.getDefaultType()

    override fun isV1SigningEnabled(): Boolean = true

    override fun isV2SigningEnabled(): Boolean = true

    override fun isSigningReady(): Boolean = true

    companion object {
        private const val PROP_STORE_FILE = "STORE_FILE"
        private const val PROP_STORE_PASSWORD = "STORE_PASSWORD"
        private const val PROP_KEY_ALIAS = "KEY_ALIAS"
        private const val PROP_KEY_PASSWORD = "KEY_PASSWORD"

        private val MANDATORY_KEYS = listOf(
            PROP_STORE_FILE, PROP_STORE_PASSWORD, PROP_KEY_ALIAS, PROP_KEY_PASSWORD
        )
    }
}
