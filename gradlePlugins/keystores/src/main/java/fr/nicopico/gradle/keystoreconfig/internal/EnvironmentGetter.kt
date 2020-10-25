package fr.nicopico.gradle.keystoreconfig.internal

internal typealias EnvironmentGetter = (String) -> String?

internal val DEFAULT_ENV_GETTER = { name: String ->
    System.getenv(name)
}

internal fun EnvironmentGetter.withPrefix(prefix: String): EnvironmentGetter =
    { name -> prefix + this(name) }
