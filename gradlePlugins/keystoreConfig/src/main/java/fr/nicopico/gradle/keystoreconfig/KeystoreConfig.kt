package fr.nicopico.gradle.keystoreconfig

import com.android.builder.model.SigningConfig
import fr.nicopico.gradle.keystoreconfig.internal.DEFAULT_ENV_GETTER
import fr.nicopico.gradle.keystoreconfig.internal.EnvironmentBackendSigningConfig
import fr.nicopico.gradle.keystoreconfig.internal.EnvironmentBackendSigningConfig.VariableNames
import fr.nicopico.gradle.keystoreconfig.internal.PropertiesBackedSigningConfig
import fr.nicopico.gradle.keystoreconfig.internal.createFileFinder
import groovy.lang.Closure
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class KeystoreConfig(
    private val name: String,
    private val project: Project
) : Named {

    override fun getName(): String = name

    var configFile: Any? = null

    var envVars: EnvironmentConfig? = null

    // region Easier configuration
    @Suppress("unused")
    fun configFile(file: Any) {
        this.configFile = file
    }

    @Suppress("unused")
    fun envVars(configuration: EnvironmentConfig.Builder.() -> Unit) {
        this.envVars = EnvironmentConfig.Builder()
            .apply(configuration)
            .build()
    }

    @Suppress("unused")
    fun envVars(configurationClosure: Closure<EnvironmentConfig.Builder>) {
        val builder = EnvironmentConfig.Builder()
        configurationClosure.apply {
            resolveStrategy = Closure.DELEGATE_FIRST
            delegate = builder
        }

        configurationClosure.call()

        this.envVars = builder.build()
    }
    // endregion

    //region Outputs

    @Suppress("unused")
    val signingConfig: SigningConfig by lazy { buildSigningConfig() }

    //endregion

    private fun buildSigningConfig(): SigningConfig {
        val fileFinder = createFileFinder(project)

        // Freeze var properties
        val configFile = this.configFile
        val envVars = this.envVars

        return when {
            configFile != null -> {
                PropertiesBackedSigningConfig(
                    name,
                    fileFinder,
                    project.file(configFile)
                )
            }
            envVars != null -> {
                EnvironmentBackendSigningConfig(
                    name,
                    VariableNames(
                        envVars.storeFile,
                        envVars.storePassword,
                        envVars.keyAlias,
                        envVars.keyPassword
                    ),
                    DEFAULT_ENV_GETTER,
                    fileFinder
                )
            }
            else -> {
                throw IllegalArgumentException("Cannot determine configuration source for $this")
            }
        }
    }
}
