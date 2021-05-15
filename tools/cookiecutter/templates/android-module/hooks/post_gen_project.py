# NOTE: With a dockerized cookiecutter, we cannot modify settings.gradle file directly
# We have to do it manually :-/

# Retrieve gradle plugin name from cookiecutter
module_name = "{{ cookiecutter.module_name }}"
module_path = "{{ cookiecutter.module_path|replace('/', ':') }}"

print("\n* Add the following line to `settings.gradle`:")
print(f"include ':{module_path}:{module_name}'")
print("and declare the module in gradle/dependencies.gradle\n")
