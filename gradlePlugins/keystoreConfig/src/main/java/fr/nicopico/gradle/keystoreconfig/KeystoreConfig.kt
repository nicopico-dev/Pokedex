package fr.nicopico.gradle.keystoreconfig

import com.android.build.api.dsl.SigningConfig
import fr.nicopico.gradle.keystoreconfig.internal.*
import fr.nicopico.gradle.keystoreconfig.internal.EnvironmentBackendSigningConfig.VariableNames
import groovy.lang.Closure
import org.gradle.api.Named
import org.gradle.api.Project

@Suppress("UnstableApiUsage")
class KeystoreConfig(
    private val name: String,
    private val project: Project
) : Named {

    override fun getName(): String = name

    //region Properties file
    var configFile: Any? = null

    @Suppress("unused")
    fun configFile(file: Any) {
        this.configFile = file
    }
    //endregion

    //region Environment variable
    var envVars: EnvironmentConfig? = null

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

    //region Debug keystore
    var debugKeystore: Any? = null

    @Suppress("unused")
    fun debugKeystore(file: Any) {
        this.debugKeystore = file
    }
    //endregion

    //region Outputs

    @Suppress("unused")
    val signingConfig: SigningConfig by lazy { buildSigningConfig() }

    //endregion

    private fun buildSigningConfig(): SigningConfig {
        val fileFinder = createFileFinder(project)

        // Freeze nullable var properties
        val configFile = this.configFile
        val envVars = this.envVars
        val debugKeystore = this.debugKeystore

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
            debugKeystore != null -> {
                SimpleSigningConfig(
                    name,
                    project.file(debugKeystore),
                    storePassword = "android",
                    keyAlias = "androiddebugkey",
                    keyPassword = "android"
                )
            }
            else -> {
                throw IllegalArgumentException("Cannot determine configuration source for $this")
            }
        }
    }
}
