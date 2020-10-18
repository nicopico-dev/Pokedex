package fr.nicopico.gradle.keystoreconfig.internal

import java.io.File
import java.util.*

@Suppress("UnstableApiUsage")
internal class PropertiesBackedSigningConfig(
    name: String,
    private val fileFinder: FileFinder,
    private val propertyFile: File
) : SigningConfigBase(name) {

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

    override var storeFile: File?
        get() = fileFinder(props.getProperty(PROP_STORE_FILE))
        set(_) { throw UnsupportedOperationException("Read-only property") }

    override var storePassword: String?
        get() = props.getProperty(PROP_STORE_PASSWORD)
        set(_) { throw UnsupportedOperationException("Read-only property") }

    override var keyAlias: String?
        get() = props.getProperty(PROP_KEY_ALIAS)
        set(_) { throw UnsupportedOperationException("Read-only property") }

    override var keyPassword: String?
        get() = props.getProperty(PROP_KEY_PASSWORD)
        set(_) { throw UnsupportedOperationException("Read-only property") }

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
