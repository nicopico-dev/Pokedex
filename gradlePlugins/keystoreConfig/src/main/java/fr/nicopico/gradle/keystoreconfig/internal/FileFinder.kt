package fr.nicopico.gradle.keystoreconfig.internal

import org.gradle.api.Project
import java.io.File

internal typealias FileFinder = (path: Any) -> File

internal fun createFileFinder(project: Project) = { path: Any -> project.file(path) }
