package fr.nicopico.gradle.keystoreconfig

import com.android.builder.model.SigningConfig
import fr.nicopico.gradle.keystoreconfig.internal.PropertiesBackedSigningConfig
import fr.nicopico.gradle.keystoreconfig.internal.createFileFinder
import org.gradle.api.Named
import org.gradle.api.Project

class KeystoreConfig(
    private val name: String,
    private val project: Project
) : Named {

    override fun getName(): String = name

    var configFile: Any? = null

    /** Allow easier configuration */
    @Suppress("unused")
    fun configFile(file: Any) {
        this.configFile = file
    }

    //region Outputs

    @Suppress("unused")
    val signingConfig: SigningConfig by lazy {
        PropertiesBackedSigningConfig(
            name,
            createFileFinder(project),
            project.file(configFile
                ?: throw IllegalStateException("Missing config file for $name")
            )
        )
    }

    //endregion
}
