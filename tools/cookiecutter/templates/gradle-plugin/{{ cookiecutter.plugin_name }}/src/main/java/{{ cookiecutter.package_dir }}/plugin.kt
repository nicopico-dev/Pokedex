package {{ cookiecutter.package_name }}

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task

open class {{ cookiecutter.plugin_classname }}Extension {
    // TODO Add plugin configuration here
}

class {{ cookiecutter.plugin_classname }} : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions
            .create("{{ cookiecutter.plugin_name }}", {{ cookiecutter.plugin_classname }}Extension::class.java)

        // TODO modify target project
        target.task("someTask", DefaultTask::class) {
            println("Hello World!")
        }
    }
}
