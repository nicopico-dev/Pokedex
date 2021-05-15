#!/usr/bin/env sh
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 TEMPLATE" >&2
    echo "  TEMPLATE: android-module, kotlin-module, gradle-plugin"
    echo "      Note:"
    echo "      modules will be created in the 'modules' directory"
    echo "      gradle-plugins will be created in the 'gradlePlugins' directory"
    exit 1
fi
cd tools/cookiecutter
./run.sh $1
cd -
