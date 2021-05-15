# NOTE: With a dockerized cookiecutter, we cannot modify settings.gradle file directly
# We have to do it manually :-/

# Retrieve gradle plugin name from cookiecutter
gradle_plugin = "{{ cookiecutter.plugin_name }}"

print("\n* Add the following line to `settings.gradle`:")
print(f"includeBuild 'gradlePlugins/{gradle_plugin}'")
print()
