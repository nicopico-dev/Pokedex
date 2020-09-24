package fr.nicopico.gradle.keystoreconfig

data class EnvironmentConfig(
    val storeFile: String,
    val storePassword: String,
    val keyAlias: String,
    val keyPassword: String
) {
    class Builder {
        lateinit var storeFile: String
        lateinit var storePassword: String
        lateinit var keyAlias: String
        lateinit var keyPassword: String

        @Suppress("unused")
        fun storeFile(value: String) {
            this.storeFile = value
        }

        @Suppress("unused")
        fun storePassword(value: String) {
            this.storePassword = value
        }

        @Suppress("unused")
        fun keyAlias(value: String) {
            this.keyAlias = value
        }

        @Suppress("unused")
        fun keyPassword(value: String) {
            this.keyPassword = value
        }

        fun build() = EnvironmentConfig(
            storeFile,
            storePassword,
            keyAlias,
            keyPassword
        )
    }
}
